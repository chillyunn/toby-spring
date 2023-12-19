어떤 클래스든 스프링의 빈으로 등록할 때 먼저 검토해야 할 것은 

**싱글톤으로 만들어져 여러 스레드에서 동시에 사용해도 괜찮은가** 하는 점이다.

**상태를 갖고 있고, 멀티스레드 환경에서 안전하지 않은 클래스**를 빈으로 무작정 등록하면 심각한 문제가 발생하기 때문이다.

기술과 서비스에 대한 추상화 기법을 이용하면 특정 기술환경에 종속되지 않는 포터블한 코드를 만들 수 있다.

UserDao와 userService는 각각 담당하는 코드의 기능적인 관심에 따라 분리되고,
서로 불필요한 영향을 주지 않으면서 독자적으로 확장이 가능하도록 만든 것이다.

=>**단일 책임 원칙(SRP)**

하나의 모듈은 한 가지 책임을 가져야 한다는 의미다(하나의 모듈이 바뀌는 이유는 한 가지여야 한다).

단일 책임 원칙의 장점. 변경이 필요할 때 수정 대상이 명확해진다.

적절하게 책임과 관심이 다른 코드를 분리하고, 서로 영향을 주지 않도록 다양한 추상화 기법을 도입하고, 애플리케이션 로직과 기술/환경을 분리하는 등의 작업이 필요하다.

=> DI

DI를 통해 PlatformTransactionManager의 생성과 의존관계 설정을 스프링에 맡긴 덕에 완벽하게 트랜잭션 기술에서 자유로운 userService를 가질 수 있게 되었다.

단일 책임 원칙을 잘 지키는 코드
 * 인터페이스 도입
 * DI
단일 책임 원칙 적용의 결과
 * 개방 폐쇄 원칙 준수
 * 모듈간 결합도가 낮아서 서로의 변경이 영향을 주지 않음
 * 변경이 단일 책임에 집중되는 응집도 높음
 * 많은 디자인 패턴이 자연스럽게 적용됨
 * 테스트하기 편함

DI는 모든 스프링 기술의 기반이 되는 핵심엔진이자 원리이며,
스프링이 지지하고 지원하는, 좋은 설계와 코드를 만드는 모든 과정에서 사용되는 가장 중요한 도구다.

스프링을 DI 프레임워크라고 부르는 이유는 스프링이 DI에 담긴 원칙과 이를 응용하는 프로그래밍 모델을 자바 엔터프라이즈 기술의 많은 문제를 해결하는데 적극적으로 활용하고 있기 때문이다.
스프링을 사용하는 개발자가 만드는 애플리케이션 코드 또한 이런 DI를 활용해서 깔끔하고 유연한 코드와 설계를 만들어낼 수 있도록 지원하고 지지해주기 때문이다.

서비스 추상화란 트랜잭션과 같이 기능은 유사하나 사용 방법이 다른 로우레벨의 다양한 기술에 대해 추상 인터페이스와 일관성 있는 접근 방법을 제공해 주는 것을 말한다.
서비스 추상화란 원활한 테스트만을 위해서도 충분히 가치가 있다.
기술이나 환경이 바뀔 가능성이 있음에도, JavaMail처럼 확장이 불가능하게 설계해놓은 API를 사용해야 하는 경우라면 추상화 계층의 도입을 적극 고려해볼 필요가 있다.
특별히 외부의 리소스와 연동하는 대부분 작업은 추상화의 대상이 될 수 있다.

테스트 대역(Test Double):테스트 환경을 만들어주기 위해, 테스트 대상이 되는 오브젝트의 기능에만 충실하게 수행하면서 빠르게, 자주 테스트를 실행할 수 있도록 사용하는 오브젝트

테스트 스텁(Test Stub): 테스트 대상 오브젝트의 의존객체로서 존재하면서 테스트 동안에 코드가 정상적으로 수행할 수 있도록 돕는 것. 대표적인 테스트 대역.

일반적으로 테스트 스텁은 메소드를 통해 전달하는 파라미터와 달리, 테스트 코드 내부에서 간접적으로 사용된다. 따라서 Di 등을 통해 미리 의존 오브젝트를 테스트 스텁으로 변경해야 한다.EX) DummyMailSender

목 오브젝트는 스텁처럼 테스트 오브젝트가 정상적으로 실행되도록 도와주면서, 테스트 오브젝트와 자신의 사이에서 일어난 커뮤니케이션 내용을 저장해뒀다가 테스트 결과를 검증하는 데 활용할 수 있게 해준다.


---
UserServiceTx는 사용자 관리라는 비즈니스 로직을 전혀 갖지 않고 고스란히 다른 UserService 구현 오브젝트에 기능을 위임한다.

트랜잭션 경계설정 코드의 분리와 DI를 통한 연결의 장점
* 비즈니스 로직을 담당하고 있는 UserServiceImpl의 코드를 작성할 때는 트랜잭션과 같은 기술적인 내용에는 전혀 신경 쓰지 않아도 된다.
* 트랜잭션의 적용이 필요한지 신경 쓰지 않아도 된다.
* JDBC나 JTA같은 로우레벨의 트랜잭션 API는 물론이고 스프링의 트랜잭션 추상화 API조차 필요 없다.
* 트랜잭션은 DI를 이용해 UserServiceTx와 같은 트랜잭션 기능을 가진 오브젝트가 먼저 실행되도록 만들기만 하면 된다.

가장 편하고 좋은 테스트 방법: 가능한 한 작은 단위로 쪼개서 테스트 하는 것
테스트가 실패했을 때 그 원인을 찾기 쉽다.
테스트의 의도나 내용이 분명해지고, 만들기도 시워진다.

테스트의 대상이 환경이나, 외부 서버, 다른 클래스의 코드에 종속되고 영향을 받지 않도록 고립시킬 필요가 있다.
테스트를 의존 대상으로부터 분리해서 고립시키는 방법: 테스트를 위한 대역 사용 ex) MailSender와 DummnyMailSender,MockMailSender`

단위 테스트: 테스트 대상 클래스를 목 오브젝트 등의 테스트 대역을 이용해 의존 오브젝트나 외부의 리소스를 사용하지 않도록 고립시켜 테스트하느 ㄴ것
통합 테스트: 두 개 이상의, 성격이나 계층이 다른 오브젝트가 연동하도록 만들어 테스트하거나, 또는 외부의 DB나 파일, 서비스 등의 리소스가 참여하는 테스트

테스트 가이드라인
* 항상 단위 테스트를 먼저 고려한다
* 외부 리소스를 사용해야만 가능한 테스트는 통합 테스트로 만든다.
* DAO는 DB를 통해 로직을 수행하는 인터페이스와 같은 역할을 하므로 DB까지 연동하는 테스트로 만드는 편이 효과적이다.
* 단위 테스트를 만들기가 너무 복잡하다고 판단되는 코드는 처음부터 통합 테스트를 고려해본다.
* 스프링 테스트 컨텍스트 프레임워크를 이용하는 테스트는 통합 테스트다.

단위 테스트를 만들기 위해서는 스텁이나 목 오브젝트의 사용이 필수적이다.
의존관계가 없는 단순한 클래스나 세부 로직을 검증하기 위해 메소드 단위로 테스트할 때가 아니라면, 대부분 의존 오브젝트를 필요로 하는 코드를 테스트하게 되기 때문이다.

Mockito 프레임워크: 번거로운 목 오브젝트를 편리하게 작성하도록 도와주는 대표적인 프레임워크