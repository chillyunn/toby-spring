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

다이내믹 프록시를 생성해주는 팩토리 빈의 장점
* 한번 부가기능을 가진 프록시를 생성하는 팩토리 빈을 만들어두면 타깃의 타입에 상관없이 재사용할 수 있다. TxProxyFactoryBean

프록시 팩토리 빈의 한계
* 트랜잭션과 같이 비즈니스 로직을 담은 많은 클래스의 메소드에 적용할 필요가 있다면 거의 비슷한 프록시 팩토리 빈의 설정이 중복되는 것을 막을 수 없다.
* 적용 대상이 늘어나면 타깃과 인터페이스만 다른 비슷한 프록시 팩토리 빈 설정 또한 늘어난다.
* TransactionHandler 오브젝트가 프록시 팩토리 빈 개수만큼 만들어진다.

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

프록시: 자신이 클라이언트가 사용하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 것
프록시의 특징: 타깃과 같은 인터페이스를 구현했다는 것과 프록시가 타깃을 제어할 수 있는 위치에 있다는 것이다.
프록시의 사용 목적:
* 클라이언트가 타깃에 접근하는 방법 제어
* 타깃에 부가적인 기능 부여
* 
데코레이터 패턴: 타깃에 부가적인 기능을 런타임 시 다이내믹하게 부여해주기 위해 프록시를 사용하는 패턴
* 컴파일시점에서는 어떤 방법과 순서로 프록시와 타깃이 연결되어 사용되는지 정해져 있지 않다. 
* 같은 인터페이스를 구현한 타깃에 여러개의 프록시를 사용할 수 있다.
* 타깃의 코드를 손대지 않고, 클라이언트가 호출하는 방법도 변경하지 않은 채로 새로운 기능을 추가할 때 유용한 방법이다.

프록시 패턴: 클라이언트가 타깃에 접근하는 방식을 변경해준다.
* 타깃의 기능을 확장하거나 추가하지 않는다.
* 타깃 오브젝트를 생성하기가 복잡하거나 당장 필요하지 않은 경우에는 꼭 필요한 시점까지 오브젝트를 생성하지 않는 편이 좋다.
그러나 타깃 오브젝트에 대한 레퍼런스가 미리 변경할 경우 적용하면 된다.
* 클라이언트에게 타깃에 대한 레퍼런스를 넘겨야 하는데, 실제 타깃 오브젝트를 만드는 대신 프록시는 넘겨주는 것이다.
* 프록시의 메소드를 통해 타깃을 사용하려고 시도하면, 그때 프록시에 타깃 오브젝트를 생성하고 요청을 위임해주는 식이다.
* 레퍼런스는 갖고 있지만 끝까지 사용하지 않거나, 많은 작업이 진행된 후에 사용되는 경우라면, 프록시를 통해 생성을 최대한 늦춤으로써 얻는 장점이 많다.
* 원격 오브젝트를 이용하는 경우: RMI나 EJB 등을 이용해 다른 서버에 존재하는 오브젝트를 사용해야할 때, 원격 오브젝트에 대한 프록시를 만들어두고, 클라이언트는 마치 로컬에 존재하는 오브젝트를 쓰는 것 처럼 프록시를 사용하게 할 수 있다.
* 클라이언트의 요청을 받으면 네트워크를 통해 원격의 오브젝트를 실행하고 결과를 받아서 클라이언트에게 돌려준다.
* 특별한 상황에서 타깃에 대한 접근권한을 제어하기 위한 경우 -> 프록시의 특정 메소드를 사용하려고 하면 예외를 발생시킨다.(Collection의 unmodifiableCollection())
* 타깃의 기능 자체에는 관여하지 않으면서 접근하는 방법을 제어

데코레이터와 프록시의 차이
* 프록시는 코드에서 자신이 만들거나 접근할 타깃 클래스 정보를 알고 있는 경우가 많다.
생성을 지연하는 프록시라면 구체적인 생성 방법을 알아야 하기 때문

프록시는 기존 코드에 영향을 주지 않으면서 타깃의 기능을 확자앟거나 접근 방법을 제어할 수 있는 유용한 방법.
그러나 많은 개발자는 프록시를 만드는 일이 번거롭게 느껴진다.
* 타깃의 인터페이스를 구현하고 위임하는 코드를 작성하기가 번거롭다. -> JDK 다이내믹 프록시
* 부가기능 코드가 중복될 가능성이 많다.

스프링의 ProxyFactoryBean은 프록시를 생성해서 빈 오브젝트로 등록하게 해주는 팩토리 빈이다.
TxProxyFactoryBean과 달리 ProxyFactoryBeanㅇ느 순수하게 프록시를 생성하는 작업만을 담당하고 프록시를 통해 제공해줄 부가기능은 별도의 빈에 둘 수 있다.

ProxyFactoryBean이 생성하는 프록시에서 사용할 부가기능은 MethodInterceptor 인터페이스를 구현해서 만든다.
InvocationHandler와 달리 invoke() 메소드는 ProxyFactoryBean으로부터 타깃 오브젝트에 대한 정보까지도 함께 제공받는다.
덕분에 타깃 오브젝트에 상관없이 독립적으로 만들어질 수 있다.
타깃이 다른 여러 프록시에서 함께 사용할 수 있고, 싱글톤 빈으로 등록 가능하다.

* 다이내믹 프록시는 프록시 팩토리에 의해 런타임 시 다이내믹하게 만들어지는 오브젝트다.
* 다이내믹 프록시 오브젝트는 타깃의 인터페이스와 같은 타입으로 ㅁ나들어진다.
* 클라이언트는 다이내믹 프록시 오브젝트를 타깃 인터페이스를 통해 사용할 수 있다.

다이내믹 프록시의 장점
* 대상 인터페이스의 메소드가 많은 경우
* 타깃 타입 제한 필요 없음.

Method.invoke()를 이용해 타깃 오브젝트의 메서드를 호출할 때는 타깃 오브젝트에서 발생하는 예외가 InvocationTargetException으로 한 번 포장돼서 전달된다.
InvocationTargetException 으로 받은 후 getTargetException() 메서드로 중첩되어 있는 예외를 가져와야 한다.

스프링은 내부적으로 리플렉션 API를 이용해서 빈 정의에 나오는 클래스 이름을 가지고 빈 오브젝트를 생성한다.
다이내믹 프록시 오브젝트는 이런 식으로 프록시 오브젝트가 생성되지 않는다. 내부적으로 새로 정의해서 사용하기 때문이다.
따라서 사전에 프록시 오브젝트의 클래스 정보를 미리 알아내서 스프링의 빈에 정의할 방법이 없다.
다이내믹 프록시는 Proxy 클래스의 newProxyInstance()라는 스태틱 팩토리 메소드를 통해서만 만들 수 있다.

스프링은 클래스 정보를 가지고 디폴트 생성자를 통해 오브젝트를 만드는 방법 외에도 빈을 만들 수 있는 여러가지 방법을 제공한다. ex) 팩토리 빈
팩토리 빈이란 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈이다.
FactoryBean 인터페이스를 구현한 클래스를 스프링의 빈으로 등록하면 팩토리 빈으로 동작한다.

MehotdInterceptor로는 메소드 정보와 함께 타깃 오브젝트가 탐긴 MethodInvocation 오브젝트가 전달된다.
MethodInvocation은 타깃 오브젝트의 메소드를 실행할 수 있는 기능이 있기 때문에
MethodInterceptor는 부가기능을 제공하는 데만 집중할 수 있따.
MethodInvocation은 일종의 콜백 오브젝트로, proceed() 메소드를 실행 시 타깃 오브젝트의 메소드를 내부적으로 실행해준다.
MethodInvocation 구현 클래스는 일종의 공유 가능한 템플릿처럼 동작한다.
=> JDK dynamic proxy를 직접 사용하는 코드와 ProxyFactoryBean을 직접 사용하는 코드의 가장 큰 차이점이자 장점.
ProxyFactoryBean은 가장 작은단위의 템플릿/콜백 구조를 으용해서 적용했기 때문에 템플릿 역할을 하는 MethodInvocation을 싱글톤으로 두고 공유 가능함.
JdbcTemplate가 SQL 파라미터 정보에 종속되지 않아 수많은 DAO 메소드가 하나의 JdbcTemplate를 공유할 수 있는 것과 같다.
ProxyFactoryBean에는 여러 개의 MethodInterceptor를 추가할 수있다.
=> ProxyFactoryBean 하나만으로 여러 개의 부가기능을 제공해주는 프록시를 만들 수 있다.
=> 새로운 부가기능을 추가할 때마다 프록시와 프록시 팩토리 빈도 추가해줘야 한다는 문제 해결
advice: MethodInterceptor처럼 타깃 오브젝트에 적용하는 부가기능을 담은 오브젝트. 타깃 오브젝트에 종속되지 않는 순수한 부가기능을 담은 오브젝트.
ProxyFactoryBean은 다이내믹 프록시와 달리 프록시가 구현해야 하는 인터페이스를 제공받지 않는다.
setInterfaces()를 통해 구현해야 할 인터페이스를 지정할 수 있지만, 자동으로 타깃 오브젝트가 구현하고 있는 인터페이스 정보를 알아내어 구현한다.

ProxyFactoryBean과 MethodInterceptor를 사용하는 방식에서 메소드 선정 기능을 사용하는 법
MethodInterceptor는 타깃 정보를 갖고 있지 않아 싱글톤 빈으로 등록 가능
-> 트랜잭션 적용 대상 메소드 이름 패턴을 넣어주면 안됨
-> MethodInterceptor에는 재사용 가능한 순수한 부가기능 제공 코드만 남기고, 프록시에 부가기능 적용 메소드를 선택하는 기능 추가하기.
-> 프록시의 핵심 가치는 타깃을 대신해서 클라이언트의 요청을 받아 처리하는 오브젝트
-> 메소드를 선별하는 기능을 프록시로부터 다시 분리하는 편이 낫다 -> 전략패턴 적용가능

ProxyFactoryBean은 두가지 확장 기능은 Advice와 Pointcut을 활용한다.
advice: 부가기능을 제공하는 오브젝트
pointcut: 메소드 선정 알고리즘을 담은 오브젝트
두 가지 모두 여러 프록시에서 공유 가능하도록 만들어지기 때문에 싱글톤 빈으로 등록 가능하다.

클라이언트 -> 프록시 -> 포인트컷 -> 어드바이스(MethodInterceptor) -> Invocation 콜백 -> 타깃 오브젝트

여러 프록시가 공유 가능, 구체적인 부가기능 방식이나 메소드 선정 알고리즘이 바뀌면 구현 클래스만 바꿔서 설정에 넣으면 됨 => OCP를 지키는 구조.
addAdvisor(pointcut,advice)를 통해 어떤 어드바이스에 대해 어떤 포인트컷을 적용할 지 선택한다.
어드바이저:포인트컷(메소드 선정 알고리즘) + 어드바이스(부가기능)

프록시 팩토리 빈 방식의 한계
* 부가기능이 타깃 오브젝트마다 새로 만들어지는 문제 -> 어드바이스
* 부가기능의 적용이 필요한 타깃 오브젝트마다 거의 비슷한 내용의 ProxyFactoryBean 설정정보를 추가해야하는 문제
-> 새로운 타깃이 등장할 때마다 설정은 매번 복사해서 붙이고 target 프로퍼티의 내용을 수정해줘야 한다.
DefaultAdvisorAutoProxyCreator는 어드바이저를 이용한 자동 프록시 생성기이다.
빈 후처리기는 ``빈 오브젝트의 프로퍼티를 강제로 수정할 수도 있고 별도의 초기화 작업, 빈 오브젝트 자체를 바꿔치기 등의 기능을 수행할 수 있다.
스프링이 생성하는 빈 오브젝트의 일부를 프록시로 포장하고, 프록시를 빈으로 대신 등록한다.

포인트컷은 오브젝트 내의 메소드를 선택하는 것과 빈 오브젝트 자체를 선택하는 두 가지 기능을 갖췄다.
포인트컷 선정 기능을 모두 적용하려면 먼저 프록시를 적용할 클래스인지 판단한 후 적용 대상 클래스인 경우 어드바이스를 적용할 메소드인지 확인한다.

transactionAdvisor빈은 DefaultAdvisorAutoProxyCreator 덕분에 ProxyFactoryBean으로 등록한 빈에서처럼 transactionAdvisor를 명시적으로 DI 하는 빈은 존재하지않는다.
어드바이저를 이용하는 자동 프록시 생성기인 DefaultAdvisorAutoProxyCreator에 의해 자동수집되고, 프록시 대상 선정 과정에 참여하며, 자동생성된 프록시에 다이내믹 하게 DI 돼서 동작하는 어드바이저가 된다.

트랜잭션 어드바이스를 적용한 프록시 자동생성기를 빈 후처리기 매커니즘을 통해 적용했다.
최소한 두 가지는 확인해야 한다.
1. 트랜잭션이 필요한 빈에 트랜잭션 부가기능이 적용됐는가 => 롤백을 통해 트랜잭션 적용 여부 테스트
2. 프록시 자동생성기가 어드바이저 빈에 연결해둔 포인트컷의 클래스 필터를 이용해서 정확히 원하는 빈에만 프록시를 생성했는가 => 포인트컷 빈의 클래스 이름 패턴을 변경해서 적용 여부 테스트

복잡하고 세밀한 기준을 이용해 클래스나 메소드를 선정하는 방법.
1. 리플렉션 API
리플렉션 API를 통해 클래스와 메소드의 이름,패키지,파라미턴,리턴 값,애노테이션,구현한 인터페이스,상속한 클래스 등의 정보를 알 수 있다.
하지만 리플렉션 API는 코드를 작성하기가 번거롭고 조건이 달라질 때마다 포인트컷 구현 코드를 수정해야한다.
2. 포인트컷 표현식: 스프링이 제공해주는 표현식 언어를 사용해서 포인트컷을 작성할 수 있는 방법.

포인트컷 표현식
AspectJExpressionPointCut 클래스 사용.
Pointcut 인터페이스를 구현해야 하는 스프링의 포인트컷은 클래스 선정을 위한 클래스 필터와 메소드 선정을 위한 메소드 매처를 각각 제공해야 한다.
NameMatchClassMethodPointcut은 클래스와 메소드의 이름패턴을 독립적으로 비교하도록 만들어져 있다.
AspectJExpressionPointcut은 클래스와 메소드의 선정 알고리즘을 포인트컷 표현식을 이용해 한 번에 지정할 수 있게 해준다.

