# Sports-simulator-2

## 개발 과정 기록

### 1. `Gradle 빌드 도구 도입 배경` 

기존 1주차 과제에서는 기본 IntelliJ 빌드를 활용하였으나, 실제로 깃허브에서 다른 자바 프로젝트나<br/>
Spring Boot 프로젝트를 살펴보면 `Gradle`빌드 도구를 많이 활용하는 것을 확인할 수 있었다.<br/>
이에 `Gradle`을 썼을 때와 `IntelliJ`를 썼을 때의 차이점이 궁금하여 찾아보니

기존 IntelliJ의 기본 빌드 방식을 사용하면 IDE의 환경에 의존하여 프로젝트를 빌드하고 실행하기에<br/>
개발자마다 설정이나 실행 환경에 따라 다른 실행 결과가 나타남을 알 수 있었다.<br/>
즉, 내 개발 환경에서는 정상 동작하지만 다른 실행 환경에서 동작하지 않는 문제가 발생할 수 있다는 것이었다.

Gradle은 프로젝트의 빌드 과정이나 의존성 관리를 코드 기반으로 표준화할 수 있다. <br/>
`build.gradle` 파일에 필요한 라이브러리와 빌드 설정을 명시함으로,<br/>
개발 환경이 달라도 동일한 방식으로 프로젝트를 빌드하고 테스트할 수 있는 것이다.

추가적으로 Spring Boot와의 연동성이나 CI/CD 환경 구축 등에 장점이 있으나<br/>
멀티스레드를 도입하며 개별 동작 과정을 테스트함에 단순 `System.out`을 통한 콘솔을 통해 확인하는 것은<br/>
다른 실행 환경에서는 문제가 발생할 수 있다는 생각이 들었기에<br/>
일관된 빌드 및 테스트 수행이 가능하다는 점에서 `Gradle` 빌드 도구를 도입하게 되었다.<br/>
(추가로 실제 프로젝트에서 많이 쓰이는 빌드 도구인 만큼 익숙해지기 위해 도입하였다)

<br/>

### 2. `1주차 피드백 반영 주요 리팩토링, 변경 사항`

#### 2-1) FootballTeam 객체를 TournamentParticipant 내부 필드로 변경
    기존 1주차에선 이차 상속을 위해 ( Team <- FootballTeam <- UefaTeam ) 구조로 클래스를 만들었으나
    UefaTeam이 가지는 의미는 축구팀의 종류가 아닌 "UEFA 토너먼트에 참가하는 팀"이라는 정보에 가까웠기에
    TournamentParticipant 클래스 필드에 FootballTeam 객체를 컴포지션하는 구조로 변경하였다.

#### 2-2) MatchService 내 fight() 메서드 개선
    MatchService의 fight() 메서드는 인자로 전달된 두 팀의 경기 진행을 의미하기에
    기존 코드의 내부 강제 형변환(UefaTeam) 과정에서 UefaTeam이 아닌 객체는 해당 메서드를 사용할 수 없었다.
    따라서 fight()의 인자로 Winnable 인터페이스를 구현한 객체를 전달할 수 있게 하여
    (우승 확률을 가진 팀만 fight()가 가능하다)
    MatchService 자체가 UEFA 토너먼트만을 위한 서비스가 아닌 전체 Match(경기)를 위한 서비스가 되도록 하였다.

#### 2-3) UEFA 참가 팀 정보, enum 합치기
    기존 코드에선 for문을 통한 순회로 idx에 따라 팀의 이름 담당 enum, 팀의 우승 확률 담당 enum을 순회하였다.
    그러나 이러한 방식은 각 enum 내부에서 순서가 하나라도 바뀌면 잘못된 정보가 들어간 객체가 만들어진다.
    이에 토너먼트 대회를 참가하는 팀의 정보(이름, 우승 확률)를 하나의 enum으로(UefaTeamInfo) 관리하도록 하였다.

#### 2-4) 가독성을 떨어뜨리는 상수화 제거
    기존 전체 코드 내에서 단순 리터럴로 숫자를 입력하여도 의미가 전달되는 부분까지 enum을 통해 상수화하였는데
    해당 부분은 오히려 가독성을 떨어뜨리기에 리터럴로 작성하여도 의미 파악이 되는 부분은 상수화를 제거하였다.

#### 2-5) setter 위험성 개선
    팀 내 부상 횟수를 설정하는 setOccurInjuryCount()와 우승 확률을 설정하는 setWinningRate()의 경우
    내부에 잘못된 값이 전달되어도 코드가 실행되어 의도와 다른 동작(에러)이 발생할 수 있다.
    이에 부상 횟수의 경우 Injurable 인터페이스를 상속하게하여 injure() 메서드를 구현하도록 강제해
    이를 구현한 TournamentParticipant에서 오직 증가만(++) 가능하도록 변경하였으며,
    Winnable인터페이스의 setWinningRate(double rate)를 구현한 곳에서 내부에 if분기를 통한 예외를 던지게 하였다.

#### 2-6) MatchService 내 fight() 과중 책임
    기존 fight() 메서드의 경우 내부에서 부상 관련 로직 + 우승 확률 조절 + 승자 결정 로직을 모두 담당하였다.
    이를 fight(Winnable teamA, Winnable teamB) 메서드 내 코드를 다음처럼 설정하여
    applyInjuries(teamA, teamB);
    return decideWinner(teamA, teamB);
    전체 경기의 흐름을 담당하게 하였고, 각 부상처리로직, 승자결정로직은 타 메서드로 책임을 변경하였다.

<br/>

### 3. `멀티스레드 기획`

가장 먼저 어느 부분을 스레드로 나누어 처리하면 성능이 향상될 수 있을 지 고민했다.

직관적으로 떠오른 부분은 각 토너먼트의 라운드마다(16강, 8강, 4강, 결승) 진행되는 매치를 각각 스레드로 관리하는 것이다.<br/>

```text
ex) 16강의 경우
    컨트롤러에서 스레드를 8개 생성
    -> 각 스레드에서 매치 진행
    -> 매치가 종료되면 컨트롤러에 종료를 알린다. (매치 종료 카운트 1 증가)
    컨트롤러에서는 매치 종료 카운트를 확인하며 카운트가 8이 되면 다음 로직(8강)을 진행한다.
```

위와 같은 방식으로 진행하면, 매치종료카운트라는 공유자원을 멀티스레드가 동시에 수정하여 동시성 문제가 발생할 수 있기에<br/>
동기화 도구를 사용하여 이를 해결할 수 있기에 학습한 개념을 적용하기에 적합하다 생각하였다.

다만, `"이러한 멀티스레드 도입이 통해 실제 성능 향상으로 이어질까?"`란 고민이 들었다.

**스레드를 생성하고, 종료하는 비용이**<br/>
각 스레드에서 매치를 처리하고 수행하도록 하여 **동시에 진행하게 함으로 얻는 이점**보다 크진 않을까?

이에 다음과 같이 멀티스레드 계획을 구상하였다.

1. 각 매치가 진행될 때 `내부 로직을 더 구체화`하여, 멀티스레드 적용의 이점을 늘린다.

```java
// 매치 구체화 기획

매치(A팀, B팀) {
    while(경기진행시간 != 풀타임) {
        공격자_선택_로직(A팀, B팀) // 공격 기회를 얻는 팀을 정한다, 기존 승리자 결정 로직을 차용 (winningRate기반 비교 방식)
        공격자_선택된_팀.공격시도(수비팀) // 공격자로 선택된 팀이 상대 팀에게 공격을 시도, 내부에서 수비팀의 수비능력치에 따라 공격 성공이 결정
        공격_결과적용() // 공격의 결과가 성공이라면 공격성공카운트 증가
        경기진행시간증가()
    }
}

```

2. 스레드를 매 라운드마다 생성하고 종료하는 비용은 너무 클 것이다. 따라서 `스레드 풀을 통해 재사용`한다.

```java
import java.util.concurrent.ExecutorService;

ExecutorService pool = Executors.newFixedThreadPool(8);
```

<br/>

### 4. `컨트롤러 추가 리팩토링`

멀티스레드를 기존 기획대로 도입하기 위해, 컨트롤러의 코드를 살펴보았으나 한 번에 구조를 알아보기 힘들었고<br/>
자연스럽게 어느 부분에 스레드를 도입해야할지 생각하는 과정이 너무 오래걸렸다.<br/>
따라서 전체적으로 가독성을 향상시킨 후 스레드를 도입하잔 생각이 들어 내부를 살펴보았다.

메서드명만 나눠져 있을 뿐 사실상 동일 동작을 수행하는 `16강 실행 메서드`와 `8강 실행 메서드`를 발견하였고<br/>
`4강 실행 메서드`, `결승 실행 메서드`를 포함하여 모든 라운드마다 크게 다음 두 가지 메서드를 실행함을 확인하였다.

- **대진표 작성(createBracket)** : `16강`, `8강`
- **경기 진행(progressMatch)** : `16강`, `8강`, `4강`, `결승`

이에 기존 `run()` 메서드 자체 구조를

```java
    public void run() {
    init();

    try {
        List<TournamentParticipant> winners = playRoundOf16();
        List<TournamentParticipant> semiFinalsTeams = playQuarterFinals(winners);

        playFinal(
                playSemiFinals(semiFinalsTeams)
        );
    } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
    }
}
```

다음처럼 변경하였고

```java
    public void run() {
        init();

        try {
            bracketAndProgressMatch(TournamentConstant.ROUND_OF_16.getValue());
            bracketAndProgressMatch(TournamentConstant.QUARTER_FINALS.getValue());
            playSemiFinals(TournamentConstant.SEMI_FINALS.getValue());
            playFinal(TournamentConstant.FINAL.getValue());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            iv.close(); // 기존엔 playFinal() 내부에 있었으나, 메서드의 동작과 관련이 없기에 finally로 이동
        }
    }
```

본래 이전 라운드 팀 목록을 인자로 받아 새로운 승자 팀 리스트 만드는 방식으로 진행하였으나,

```java
//private final List<TournamentParticipant> totalTeams = new ArrayList<>();
private List<TournamentParticipant> teams = new ArrayList<>();
```

기존 `totalTeams` 필드에서 `final`키워드를 제거하여 동일 변수를 재활용하도록 하였다.<br/>
또한 라운드 진행에 해당하는 메서드의 매개변수로 enum(`TournamentConstant`) 상수를 활용하여,<br/>
가독성을 증가시키고자 하였다.

하단은 수정된 각 라운드의 내부 코드이다.

```java
private void bracketAndProgressMatch(int roundInfo) {
    ov.displayTeamsMessage(teams);
    teams = createBracket(teams.size(), teams);
    teams = progressMatch(roundInfo, teams);
}

private void playSemiFinals(int roundInfo) {
    ov.displayTeamsMessage(teams);
    teams = progressMatch(roundInfo, teams);
}

private void playFinal(int roundInfo) {
    teams = progressMatch(roundInfo, teams);
    ov.finalWinnerMessage(teams.getFirst());
}
```

이전 컨트롤러와의 차이점도 살펴보고, 새 컨트롤러 동작 중 문제가 발생하면 참고하고자<br/>
기존 `UefaController`는 남겨두고, 새로운 `EnhancedUefaController`를 생성하였다.

<br/>

### 5. `멀티 스레드 적용 과정 : ExecutorService`

전체 토너먼트 시나리오의 흐름을 담당하는 컨트롤러 내 `progressMatch()`메서드에서 각 매치를 진행한다.

```java
private List<TournamentParticipant> progressMatch(int teamsCount, List<TournamentParticipant> teams) {
    List<TournamentParticipant> winners = new ArrayList<>();

    int roundMatchCount = 0;
    for (int i = 0; i < teamsCount; i += 2) {
        ov.printMatchInfo(teamsCount, ++roundMatchCount, teams.get(i), teams.get(i + 1));
        pressAnyKey();

        TournamentParticipant winner = ms.fight(
                teams.get(i),
                teams.get(i + 1)
        );
        winners.add(winner);
        ov.printWinner(winner);
    }

    return winners;
}
```

이때 각 경기의 매치를 멀티스레드로 진행하며 스레드풀을 이용해 라운드마다 생성, 종료 비용을 줄이고자<br/>
`ExecutorService` 인터페이스를 활용했으면 구현체로 `newFixedThreadPool`을 사용해 스레드 수를 고정하였다.

```java
// 컨트롤러의 멤버
private final ExecutorService pool = Executors.newFixedThreadPool(8);
```

스레드를 적용하기 앞서 progressMatch 내에서 입출력 관련 코드를 따로 빼내어<br/>
모든 연산이 진행되고 난후 입출력이 발생하게 수정하였다. (연산 부에 스레드를 적용하기 위해)

```java
private List<TournamentParticipant> progressMatch(int teamsCount, List<TournamentParticipant> teams) {
        List<TournamentParticipant> winners = new ArrayList<>();
        List<TournamentParticipant> losers = new ArrayList<>();
```

```java
    // 연산 부
    for (int i = 0; i < teamsCount; i += 2) {
        TournamentParticipant winner = ms.fight(
                teams.get(i),
                teams.get(i + 1)
        );

        TournamentParticipant loser = (winner == teams.get(i)) ? teams.get(i + 1) : teams.get(i);
        winners.add(winner);
        losers.add(loser);
    }
```
```java
    // 입출력 부
    int roundMatchCount = 0;
    for (int i = 0; i < teamsCount; i += 2) {
        ov.printMatchInfo(teamsCount, roundMatchCount + 1, teams.get(i), teams.get(i + 1));
        pressAnyKey();
        ov.printMatchResult(winners.get(roundMatchCount), losers.get(roundMatchCount++));
    }

    return winners;
}
```

이후 만들어 둔 스레드풀을 적용한 `progressMatch()` 메서드 내부 코드는 다음과 같다.

```java
List<Future<?>> futures = new ArrayList<>();
for (int i = 0; i < teamsCount; i += 2) {
    final int idx = i;
    futures.add(pool.submit(() -> {
        TournamentParticipant winner = ms.fight(
                teams.get(idx),
                teams.get(idx + 1)
        );
        TournamentParticipant loser = (winner == teams.get(idx)) ? teams.get(idx + 1) : teams.get(idx);

        synchronized (winners) {
            winners.add(winner);
        }
        synchronized (losers) {
            losers.add(loser);
        }
    }));
}
```

### 6. `멀티스레드 적용 성능 개선 확인`

실제로 멀티스레드를 통한 동시 실행이 성능적 개선이 있는 지 확인해보기 위해<br/>

```java
long start = System.currentTimeMillis();

    (매치 로직)

long end = System.currentTimeMillis();
```

간단한 `System` 클래스의 `currentTimeMillis()` 메서드를 통해<br/>
멀티스레드 적용 전과 멀티스레드 적용 후의 차이를 확인해보았다.

![시간 비교](image/first_time_check.png)

빨간색은 해당 라운드에서 가장 오래 걸린 시간이며, 초록색은 해당 라운드에서 가장 빠르게 처리된 시간이다.

표본이 적은 것도 문제가 있겠지만, 위 데이터에서 아래와 같은 문제점들을 발견하였다.

1. **매치 로직에서 무승부 발생과 발생하지 않은 경우의 시간적 차이가 크다.**

    ```java
    int shootoutTime = rd.nextInt(FootballConstant.SHOOTOUT_TIME.getValue());
        try {
            Thread.sleep(shootoutTime);
            ...
    ```

    위 코드는 `MatchService` 내부 무승부 처리 로직이다. 무승부가 발생할 경우<br/>
    해당 스레드의 지연시간을 최대 **SHOOTOUT_TIME**(ms)만큼 발생하게 하였는데 이러한 스레드 지연시간이<br/>
    무승부가 발생하지 않을 경우의 로직 처리 시간보다 훨씬 크기에, 실행 시 마다 상이한 결과 데이터를 얻는 것이었다. 


2. **무승부가 아닌 경기만 존재할 경우, 해당 라운드의 시간 비교가 힘들다.**

    무승부가 아닌 매치들만 해당 라운드의 존재할 경우, **ms** 단위로는<br/>
    대부분 `0ms`, `1ms`의 데이터만 존재하였다. 따라서 스레드 적용으로 인한 확실한 차이를 확인하기 어려웠다.

이에 멀티스레드 적용을 통한 시간적 차이를 확인하기 위해<br/>

- **무승부 발생 시 스레드 대기 시간을 0으로 하고 (테스트를 위해)**
- `currentTimeMillis()`가 아닌 `nanoTime()`을 적용해 보다 정밀한 측정을 하기로 하였다.

---
