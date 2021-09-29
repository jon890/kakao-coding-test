package com.bifos.kakao;

import com.bifos.kakao.constant.MatchingConfig;
import com.bifos.kakao.dto.*;
import com.bifos.kakao.network.KakaoClient;
import com.bifos.kakao.network.MatchingRestful;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws IOException {
//        run(1);
        run(2);
    }

    /**
     * 해당 Retrofit Response 가 정상 응답을 반환했는지 확인한다
     * 정상 응답을 반환하지 않으면 다음으로 진행할 수 없기 때문에
     * RuntimeException 을 던진다
     *
     * @param response
     * @param <T>
     */
    private static <T> void isSuccessful(Response<T> response) {
        if (!response.isSuccessful()) {
            try {
                System.out.println(response.errorBody().string());
                throw new RuntimeException(response.getClass() + " 요청이 실패하였습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void run(int problemId) throws IOException {
        KakaoClient kakaoClient = new KakaoClient(MatchingConfig.BASE_URL);
        MatchingRestful matchingApi = kakaoClient.getClient();

        Response<StartResponseDto> startResponse = matchingApi.start(new StartRequestDto(problemId)).execute();
        isSuccessful(startResponse);

        StartResponseDto startResponseDto = startResponse.body();
        String authKey = startResponseDto.getAuthKey();
        int time = startResponseDto.getTime();
        ServerState serverState = ServerState.Ready;

        Map<Integer, Integer> userGradeMap = new HashMap<>();
        LinkedList<WaitingLineResponseDto.WaitingInfo> waitingList = new LinkedList<>();

        updateUserInfoMap(userGradeMap, matchingApi.getUserInfo(authKey).execute());
        userGradeMap.replaceAll((i, v) -> 5000);

        while (serverState == ServerState.Ready && time <= MatchingConfig.FINISH_TIME) {
            // 매칭이 가능한 시간이면 대기큐 업데이트
            if (time <= MatchingConfig.LAST_MATCHING_TIME)
                waitingList = updateWaitingList(matchingApi.getWaitingLine(authKey).execute());

            Response<MatchResponseDto> matchResponse;
            if (waitingList.size() < 2) {
                // 매칭 시킬 사람이 없으면 시간을 흘려보낸다
                matchResponse = matchingApi.match(authKey, MatchRequestDto.getNullRequest()).execute();
            } else {
                waitingList = waitingList.stream()
                        .sorted((o1, o2) -> {
                            // 대기 시간으로 정렬
                            if (o1.getFrom() != o2.getFrom()) {
                                return o1.getFrom() - o2.getFrom();
                            } else {
                                // 점수 순으로 정렬
                                int skill1 = userGradeMap.get(o1.getUserId());
                                int skill2 = userGradeMap.get(o2.getUserId());
                                return skill1 - skill2;
                            }
                        })
                        .collect(Collectors.toCollection(LinkedList::new));

                List<List<Integer>> matchPairList = new ArrayList<>();

                if (problemId == 1) {
                    // 무작정 매칭 시킨다
                    while (waitingList.size() > 1) {
                        WaitingLineResponseDto.WaitingInfo user1 = waitingList.poll();
                        WaitingLineResponseDto.WaitingInfo user2 = waitingList.poll();
                        matchPairList.add(List.of(user1.getUserId(), user2.getUserId()));
                    }
                } else if (problemId == 2) {
                    boolean[] matched = new boolean[waitingList.size()];
                    for (int i = 0; i < waitingList.size() - 1; i++) {
                        if (matched[i] || matched[i + 1]) continue;

                        WaitingLineResponseDto.WaitingInfo user1 = waitingList.get(i);
                        WaitingLineResponseDto.WaitingInfo user2 = waitingList.get(i + 1);

                        int skill1 = userGradeMap.get(user1.getUserId());
                        int skill2 = userGradeMap.get(user2.getUserId());
                        
                        // 점수가 비슷하거나
                        // 매칭 대기시간이 끝나가는 유저를 매칭시킨다
                        if (Math.abs(skill1 - skill2) < 200 ||
                                Math.abs(user1.getFrom() - time) > 10 ||
                                Math.abs(user2.getFrom() - time) > 10) {
                            matchPairList.add(List.of(user1.getUserId(), user2.getUserId()));
                            matched[i] = true;
                            matched[i + 1] = true;
                        }
                    }
                }
                matchResponse = matchingApi.match(authKey, new MatchRequestDto(matchPairList)).execute();
            }

            // 매칭 요청을 보내고 서버 상태, 시간 업데이트
            isSuccessful(matchResponse);
            MatchResponseDto matchResponseDto = matchResponse.body();
            serverState = ServerState.ofValue(matchResponseDto.getStatus());
            time = matchResponseDto.getTime();

            // 게임 결과를 확인하고 업데이트
            Response<GameResultResponseDto> gameResultResponse = matchingApi.getGameResult(authKey).execute();
            int updateCount = updateGameResult(userGradeMap, gameResultResponse);
            if (updateCount > 0) {
                // 매칭 결과를 서버에 업데이트
                Response<ChangeGradeResponseDto> changeGradeResponse = matchingApi.changeGrade(authKey, getChangeGradeRequest(userGradeMap)).execute();
                isSuccessful(changeGradeResponse);
            }
        }

        // 점수 출력후 종료
        Response<ScoreResponseDto> scoreResponse = matchingApi.getScore(authKey).execute();
        isSuccessful(scoreResponse);

        ScoreResponseDto scoreResponseDto = scoreResponse.body();
        System.out.println("########## 종료 ##########");
        System.out.println("토큰 앞 6자리: " + MatchingConfig.X_AUTH_TOKEN.substring(0, 6));
        System.out.println("실행 시나리오 : " + problemId);
        System.out.println("서버 상태 : " + scoreResponseDto.getStatus());
        System.out.println("효율성 점수 : " + scoreResponseDto.getEfficiencyScore());
        System.out.println("정확성 점수1 : " + scoreResponseDto.getAccurancyScore1());
        System.out.println("정확성 점수2 : " + scoreResponseDto.getAccurancyScore2());
        System.out.println("총점 : " + scoreResponseDto.getTotal());
        System.out.println("########## 종료 ##########");
    }

    private static void updateUserInfoMap(Map<Integer, Integer> map, Response<UserInfoResponseDto> response) throws RuntimeException {
        isSuccessful(response);

        UserInfoResponseDto userInfoResponseDto = response.body();
        for (UserInfoResponseDto.UserInfo userInfo : userInfoResponseDto.getUserInfoList()) {
            map.put(userInfo.getId(), userInfo.getGrade());
        }
    }

    private static LinkedList<WaitingLineResponseDto.WaitingInfo> updateWaitingList(Response<WaitingLineResponseDto> response) throws RuntimeException {
        isSuccessful(response);
        return new LinkedList<>(response.body().getWaitingInfoList());
    }

    private static int updateGameResult(Map<Integer, Integer> map, Response<GameResultResponseDto> response) {
        isSuccessful(response);

        GameResultResponseDto gameResultResponseDto = response.body();
        List<GameResultResponseDto.GameResultInfo> gameResultInfoList = gameResultResponseDto.getGameResultInfoList();

        for (GameResultResponseDto.GameResultInfo gameResultInfo : gameResultInfoList) {
            int winId = gameResultInfo.getWinId();
            int loseId = gameResultInfo.getLoseId();
            int taken = gameResultInfo.getTaken();

            // 걸린시간으로 부터 고유 실력 차를 알아낸다
            double diff = 0.0;

            // 가중치를 좀더 섬세하게 구하자
            // 랜덤이지만 많이 돌리면 더 좋은 결과가 나오지 않을까?
            for (int i = 0; i < 5; i++) {
                diff += (40 - taken + Math.random() * 5) / 35 * 99000;
                diff += (40 - taken - Math.random() * 5) / 35 * 99000;
            }
            diff /= 10;

            // 0 ~ 99,000의 결과가 나오므로
            // 1000으로 나누자
            double mid = (int) (diff / (2 * 1000));
            double winRate = (double) map.get(winId) / (map.get(winId) + map.get(loseId));
            double loseRate = 1 - winRate;

            // 이길 확률, 질 확률을 계산하여 교정해준다
            int winCorrection = (int) (mid * winRate);
            int loseCorrection = (int) (mid * loseRate);

            map.put(winId, Math.min(map.get(winId) + winCorrection, 9999));
            map.put(loseId, Math.max(map.get(loseId) - loseCorrection, 0));
        }

        return gameResultInfoList.size();
    }

    private static ChangeGradeRequestDto getChangeGradeRequest(Map<Integer, Integer> map) {
        List<ChangeGradeRequestDto.Command> commandList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ChangeGradeRequestDto.Command command = ChangeGradeRequestDto.Command.builder()
                    .id(entry.getKey())
                    .grade(entry.getValue())
                    .build();
            commandList.add(command);
        }
        return new ChangeGradeRequestDto(commandList);
    }
}