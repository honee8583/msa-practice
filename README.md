### 1. 라우팅 (Routing) 🛣️

Spring Cloud Gateway는 외부 요청을 각기 다른 마이크로서비스로 라우팅하는 핵심 역할을 합니다. 이를 통해 클라이언트는 단일 진입점(Gateway)만 알고 있으면 되며, 복잡한 서비스들의 주소를 일일이 관리할 필요가 없습니다. Gateway는 요청을 적절한 서비스 인스턴스로 전달하여 부하를 분산시키고, 서비스의 주소가 변경되더라도 Gateway의 설정만 수정하면 되므로 유연성이 높아집니다.

<br/>

### 2. 보안 및 인증/인가 (Security) 🛡️

Gateway는 모든 요청이 들어오는 첫 관문이기 때문에, 중앙 집중식으로 보안 로직을 처리하기에 매우 적합합니다. 예를 들어, JWT(JSON Web Token) 검증, OAuth2 인증, API 키 검증 등의 로직을 Gateway에 구현하여 각 마이크로서비스는 비즈니스 로직에만 집중할 수 있게 됩니다. 이는 보안 관리를 훨씬 단순하게 만들고 중복 코드를 줄여줍니다.

<br/>

### 3. API 공통 기능 처리 (Cross-Cutting Concerns) 💡

Gateway는 여러 마이크로서비스에 공통적으로 적용되는 기능들을 한 곳에서 처리할 수 있습니다. 대표적인 예시는 다음과 같습니다.

- **로깅 (Logging):** 모든 요청에 대한 로그를 Gateway에서 수집하여 전체 시스템의 흐름을 파악하기 쉽습니다.
- **모니터링 (Monitoring):** 요청의 지연 시간, 성공/실패 여부 등을 Gateway에서 모니터링하여 시스템 상태를 한눈에 볼 수 있습니다.
- **속도 제한 (Rate Limiting):** 특정 API에 대한 요청량을 제한하여 시스템 과부하를 방지할 수 있습니다.
- **추적 (Tracing):** `Correlation ID`와 같은 추적 식별자를 모든 요청에 추가하여 분산된 서비스 간의 요청 흐름을 추적할 수 있습니다.

<br/>

### 4. 동적 라우팅 및 설정 (Dynamic Routing) 🔄

Spring Cloud Gateway는 서비스 디스커버리(예: Eureka, Consul)와 통합하여 동적으로 라우팅 설정을 변경할 수 있습니다. 새로운 서비스 인스턴스가 추가되거나 기존 서비스가 삭제될 때, Gateway는 자동으로 변경사항을 감지하여 라우팅 규칙을 업데이트합니다. 이는 **무중단 배포**와 **스케일 아웃/인**을 용이하게 만들어줍니다.

<br/>

### 5. 장애 격리 및 회복성 (Fault Tolerance) 🔗

Gateway는 Circuit Breaker(회로 차단기) 패턴을 적용하여 특정 서비스에 장애가 발생했을 때, 해당 서비스로의 요청을 차단하고 미리 정의된 대체 응답(fallback)을 반환할 수 있습니다. 이를 통해 하나의 서비스 장애가 전체 시스템으로 확산되는 것을 막고, 시스템의 회복성을 높여줍니다.

<br/>

## Eureka(Service Discovery)서버 생성하기


### ✅ Service Discovery의 역할

- **서비스 등록 (Service Registration):**
다른 마이크로서비스(Eureka 클라이언트)들이 자신의 위치(IP 주소, 포트 번호 등)를 이 서버에 등록합니다. 이렇게 등록된 서비스들은 서로를 찾을 수 있게 됩니다.
- **서비스 조회 (Service Discovery):**
서비스가 다른 서비스와 통신해야 할 때, 이 Eureka 서버에 해당 서비스의 위치를 질의합니다. Eureka 서버는 등록된 서비스 목록에서 요청된 서비스의 위치를 알려줍니다.
- **상태 관리 (Health Check):**
Eureka 서버는 등록된 모든 서비스 인스턴스들의 상태를 주기적으로 확인(Health Check)합니다. 만약 특정 서비스 인스턴스가 다운되면, 해당 인스턴스를 서비스 목록에서 제거하여 실패한 요청이 가지 않도록 합니다.

<br/>

### 1. netflix-eureka-server 의존성 주입

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
```
<br/>

### 2. 메인클래스에 어노테이션 추가

```groovy
@EnableEurekaServer
@SpringBootApplication
public class FarmdoraDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmdoraDiscoveryServiceApplication.class, args);
    }

}
```

메인클래스에 `@EnableEurekaServer` 어노테이션을 선언해 해당 프로젝트(서비스)는 디스커버리 서비스 역할을 하는 서버임을 명시한다.

<br/>

### 3. yaml 설정 추가

```yaml
server:
  port: 8761

spring:
  application:
    name: farmdora-discovery-service  # 애플리케이션명을 마이크로서비스의 고유아이디로 지정

eureka:
  client:
    register-with-eureka: false  # Eureka 서버에 자신을 등록하지 않겠다는 의미 (기본값 : true)
    fetch-registry: false        # Eureka 서버로부터 다른 마이크로서비스들의 정보를 가져오지 않겠다는 의미 (기본값 : true)
```

- 애플리케이션명을 설정해 마이크로서비스의 고유아이디로 지정할 수 있습니다.
- 해당 서버는 디스커버리 서비스임으로 자신을 Eureka 서버에 등록하지 않습니다.
- 다른 서비스들의 정보를 가져올 필요 없으며, 다른 서비스들이 이 서버에 정보를 제공합니다.

<br/>

<img width="2541" height="1224" alt="Image" src="https://github.com/user-attachments/assets/cebab725-8e27-4cd2-befd-e8fce36162c9" />

localhost:8761로 접속하면 해당 화면이 뜨는 것을 확인할 수 있습니다.

<br/>

## Eureka 서버에 마이크로서비스 등록하기


### 1. netflix-eureka-client 의존성 추가하기

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
```

<br/>

### 2. 메인클래스에 어노테이션 추가

```java
@EnableDiscoveryClient
@SpringBootApplication
public class FarmdoraProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmdoraProductServiceApplication.class, args);
    }

}
```

Eureka서버에서 선언한 `@EnableEurekaServer`와 달리 `@EnableDiscoveryClient` 어노테이션을 선언해줍니다.

<br/>

### 3. yaml 설정 추가

```yaml
server:
  port: 9001

spring:
  application:
    name: product-service

eureka:
  client:
    register-with-eureka: true  # Eureka 서버에 이 서버를 등록한다.
    fetch-registry: true  # Eureka 서버로부터 다른 인스턴스들의 정보를 주기적으로 가져온다.
    service-url:
      defaultZone: http://localhost:8761/eureka  # Eureka 엔드포인트에 현재 마이크로 서비스의 정보를 등록한다.
```

- Eureka 서버에 이 서버를 등록합니다.
- Eureka 서버로부터 다른 인스턴스들의 정보를 주기적으로 가져오는 설정입니다. 다른 서비스와 통신할 때 사용하게 됩니다.
- 해당 엔드포인트로 자신의 정보를 등록하고 다른 서비스의 정보를 가져옵니다.

<br/>

### 실행화면

<img width="2542" height="1222" alt="Image" src="https://github.com/user-attachments/assets/f4d0ec30-d404-436f-a82f-c1c096836eb3" />

해당 서버를 실행하고 다시 localhost:8761로 접속하면 실행한 서버를 볼 수 있습니다. IP주소, 애플리케이션명, 포트번호를 확인할 수 있습니다.

<br/>

<img width="2543" height="1222" alt="Image" src="https://github.com/user-attachments/assets/20d89e20-75dd-4151-871b-27a415dc8093" />

다른 포트로 새롭게 서버를 실행하게되면 위와 같이 2개의 서버가 등록된 모습을 볼 수 있습니다.

<br/>

## Netflix Ribbon, Zuul

Netflix에서 만든 Netflix Ribbon과 Netflix Zuul은 마이크로서비스 아키텍처(MSA)에서 중요한 역할을 하는 두 가지 핵심 컴포넌트입니다.

<br/>

### ✅ Netflix Ribbon

**Ribbon**은 클라이언트 측 로드 밸런서입니다. 마이크로서비스가 다른 서비스를 호출할 때 Ribbon은 호출할 서비스의 여러 인스턴스 중 하나를 선택해주는 역할을 합니다.

Netflix Ribbon은 2019년에 개발이 중단되었습니다. 스프링 클라우드 생태계에서는 Ribbon의 역할을 대신하는 **Spring Cloud Load Balancer**가 등장했습니다. Spring Cloud Load Balancer는 스프링 부트 2.4 버전부터 기본 로드 밸런서로 채택되어, 현재는 이 기술을 사용하는 것이 권장됩니다.

<br/>

### ✅ Netflix Zuul (API Gateway)

Zuul은 API 게이트웨이(API Gateway) 역할을 합니다. 모든 외부 클라이언트(웹, 모바일 등)의 요청을 받는 유일한 진입점(Entry Point)입니다. 외부 요청은 Zuul을 통과한 후 Zuul의 라우팅 규칙에 따라 적절한 내부 마이크로서비스로 전달됩니다.

Netflix Zuul 또한 현재는 유지보수가 중단된 상태입니다. Zuul 1은 블로킹(Blocking) I/O 모델을 사용하여 성능상 한계가 있었고, 이를 개선한 Zuul 2가 나왔으나 스프링 클라우드에서는 Zuul을 대체하는 **Spring Cloud Gateway**가 공식적인 API 게이트웨이로 자리 잡았습니다.

- **Netflix Ribbon**의 대체재: **Spring Cloud Load Balancer**
- **Netflix Zuul**의 대체재: **Spring Cloud Gateway**

<br/>

## Spring Cloud Gateway 적용하기

Spring Cloud Gateway를 적용하면 단일 엔드포인트로 마이크로서비스로 요청을 보낼 수 있습니다. 즉, localhost:8081, localhost:8082로 직접 요청을 하는 것이 아니라, gateway 서버의 포트가 8000번인경우 localhost:8000/product-service/helloword 와 같이 단일 진입점(localhost:8000)으로 각 서비스로 요청을 보낼 수 있게됩니다.

<br/>

### 1. Gateway 의존성 추가하기

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
```

새로운 프로젝트를 생성하고 위의 의존성을 추가해줍니다.

<br/>

### 2. yaml 설정하기

```yaml
server:
  port: 8000

spring:
  application:
    name: gateway-service

  cloud:
    gateway:
      server:
        webmvc:
          routes:
            - id: user-service
              uri: http://localhost:8081/  # 어디로 이동시킬것인지
              predicates:
                - Path=/user-service/**  # 어떤 정보(url 패턴)로 들어온 경우
            - id: product-service
              uri: http://localhost:8082/  # 어디로 이동시킬것인지
              predicates:
                - Path=/product-service/**  # 어떤 정보(url 패턴)로 들어온 경우

```

- 8000번 포트로 서버를 실행합니다.
- 애플리케이션 명을 지정해줍니다.
- user-service를 `id`로 가지고 있는 서버로 라우팅합니다.
    - `predicates`에 설정한 패턴으로 url이 요청이 되면 `uri`에 해당하는 서버로 라우팅시켜줍니다.
- product-service 또한 마찬가지로 라우팅을 설정해줍니다.

<br/>

### 3. Rest Api 생성하기

각 마이크로서비스로 요청이 되고 있는지 확인하기 위해 각 서버마다 Api를 생성해주고 각각 다른 값을 리턴하도록 합니다.

```java
// product-service
@RestController
@RequestMapping("/product-service")
public class ProductController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Product Service!!";
    }
}

// user-service
@RestController
@RequestMapping("/user-service")
public class UserController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the User Service!!";
    }
}
```

각 서버마다 같은 경로로 다른 문자열을 리턴해주도록 합니다. Api의 시작점은 각각 “/product-service”, “/user-service”로 설정했습니다.

<br/>

### 4. 서버 실행하기

기존에 생성한 Eureka서버(서비스 디스커버리 서버), 마이크로서비스들, 그리고 현재 생성한 gateway서버를 실행해줍니다.

`http://localhost:8000/user-service/welcome`으로 요청하면 “Welcome to the User Service!!” 문자열을 리턴받아야하고,

`http://localhost:8000/product-service/welcome`으로 요청하면 “Welcome to the Product Service!!” 문자열을 리턴받아야합니다.

<br/>

<img width="446" height="85" alt="Image" src="https://github.com/user-attachments/assets/51000b01-41ef-440d-bfc7-ffea3646a940" />

<img width="437" height="82" alt="Image" src="https://github.com/user-attachments/assets/f15c0832-ec03-4d6d-adba-0156968be296" />

이렇게 특정 마이크로서비스로 라우팅되어 각 문자열을 리턴받는 모습을 확인할 수 있습니다.
