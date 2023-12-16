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