# ORM이란?

ORM은 객체와 관계형 데이터베이스의 테이블을 자동으로 매핑해주는 기술을 말한다.

객체 지향 프로그래밍은 클래스를 사용하고, 관계형 데이터베이스(DBMS)는 테이블을 사용한다. 따라서 객체 모델과 관계형 모델 사이에는 불일치가 발생한다. ORM은 객체 간의 관계를 바탕으로 SQL을 자동으로 생성하여 이러한 불일치를 해결해주는 역할을 한다. 객체를 통해 간접적으로 DB를 다루기 때문에 Persistant API라고도 부르며, JPA, Hibernate 등이 있다.

객체 지향적인 코드로 인해 더 직관적이고, 비즈니스 로직에 더 집중할 수 있게 도와준다. SQL 쿼리를 직접 쓰는게 아닌 직관적인 코드로 데이터를 조작할 수 있기 때문에 선택과 집중에 유리하다. 또한 재사용성 및 유지보수의 편리성이 증가하고 DBMS에 대한 종속정이 줄어드는 장점이 있다. 하지만 설계를 매우 신중하게 진행해야 하며 프로젝트의 복잡성이 커질 경우 난이도가 올라갈 수 있다. 프로시저가 많은 시스템에서는 ORM의 객체 지향적인 장점을 활용하기 어려울 수도 있다.

## Impedance Mismatch

객체와 관계 사이에 매핑을 시도하거나 통합할 때 충돌하거나 불일치하는 문제가 발생할 수 있는데, 이를 임피던스 불일치(Impedance Mismatch)라고 한다.

### 1. 세분성 (**Granularity)**

```sql
CREATE TABLE Contact (
	id integer not null,
	first varchar(255),
	last varchar(255),
	middle varchar(255),
  notes varchar(255),
  starred boolean not null,
  website varchar(255),
  primary key (id)
)
```

이런 속성을 가진 테이블이 있다고 하자. 객체에서는 Contact라는 클래스 안에 저 속성을 전부 넣을 수도 있지만…

```sql
public class Contact {
    private Integer id;
    private Name name;
    private String notes;
    private URL website;
    private boolean starred;
}

public class Name {
    private String first;
    private String middle;
    private String last;
}
```

first, middle, last는 이름과 관련된 속성이다. name, notes, id 등은 contact와 관련된 속성이다. 하나의 클래스는 그 주제에 맞는 원소들만 가지고 있는 것이 바람직하므로 클래스는 두 개로 쪼개졌다. 이제 테이블은 하나지만 클래스는 두 개가 되었다.

### 2. 상속 (Subtypes)

RDBMS에는 상속이라는 개념이 존재하지 않는다. 비슷한 기능이 존재하긴 하지만 표준화되어있지 않다.

### 3. 동일성 (Identity)

RDBMS는 정확히 하나의 동일성을 보장하는 개념인 PK를 제공한다. 하지만 자바는 동일성을 나타내는 == 뿐만 아니라 동등성을 나타내는 equals()를 모두 정의한다.

### 4. 연관성 (Associations)

객체지향 언어에서 연관관계는 단방향 참조로만 이루어진다. 하지만 RDBMS는 FK를 사용해서 양방향 참조가 가능하다. 객체 모델로 양방향 참조를 구현하려면 양쪽에 연관을 두 번 정의해야 한다.

### 5. 데이터 탐색 (Data navigation)

자바에서 데이터에 접근할 때는 하나의 객체에서 출발해 다른 연결로 이어지는 객체 그래프 탐색 방식을 사용하지만, RDBMS에서는 비효율적인 방법이다. RDBMS는 JOIN을 통해 여러 엔티티를 불러와서 데이터를 탐색한다.

이러한 문제를 어떻게 해결해야 할까?

1. SQL을 사용하지 않는다. MongoDB나 key-value 형식을 이용하는 Redis를 사용하면 ORM도 필요 없다.
2. 함수형 언어로 구현된 ORM을 이용한다.
3. Runtime OO-relational layering : OO객체를 런타임에 테이블(tuple, row)로 매핑한다. 이게 바로 Hibernate/JPA가 하는 일.

데이터 전송/검증/표현 자동화, 코드량이 줄어들며 네임스페이스와 의미 매핑에 용이하다. JOIN, CASCADE 등을 ORM이 알아서 처리해주는 장점이 있지만, 프레임워크 레이어가 끼는 만큼 성능 오버헤드가 존재하며, OO 개념인 상속/다형성은 DB세계에서 자연스럽지 않다는 한계는 극복하기 힘들다. 따라서, ORM이 이러한 mismatch 문제를 완전히 해결하기보다는 프레임워크 등을 이영하여 부족한 부분을 Compensation 하는 방식으로 나아가야 한다.

## ORM, JPA, Hibernate, Spring Data JPA 정리

### ORM

개념/철학의 관점임. 객체를 DB 테이블로 자동 매핑하는 기술을 의미함. SQL을 직접 쓰지 않고도 DB 작업을 객체로 처리하는 아이디어 전체를 의미한다.

### JPA(Java Persistence API)

ORM을 위한 JAVA 표준 인터페이스이다. JPA는 인터페이스 + 표준 명세로 이루어져 있다. 예를 들어 @Entity 는 무슨 역할을 하는지, @Id, EntityManager 등등… 이런 것들은 모두 JPA의 규약이다. 따라서, JPA는 약속/명세서일 뿐이다.

### Hibernate

JPA 인터페이스를 실제로 구현시켜줄 구현체이다. Hibernate는 SQL 생성, DB와 통신, 영속성 컨텍스트, Dirty Checking, Flush 등등의 일을 수행한다. 즉, JPA가 규약이라면 Hibernate는 실제로 일하는 엔진이다.

### EntityManager

JPA에서 DB 작업을 담당하는 핵심 API이자, 영속성 컨텍스트를 관리하는 관리자이다.

1. 영속성 컨텍스트 생성 및 관리
    1. persist() 실행 → 엔티티를 영속성 컨텍스트에 저장한다.
    2. find() 실행 → 영속성 컨텍스트 1차 캐시 또는 DB에서 조회한다.
    3. remove() 실행 → 삭제 상태로 표시한다.
2. 트랜잭션과 연동
    1. 트랜잭션 시작 → 영속성 컨텍스트 활성화
    2. 트랜잭션 commit → flush → 실제 DB 반영
3. flush() 실행
    1. 변경 감지(Dirty Checking) 수행 → 변경된 엔티티를 찾는다.
    2. 필요한 SQL을 생성한다.
    3. DB에 SQL을 전송한다
4. JPQL 실행
    1. EntityManager는 JPQL을 SQL로 변환해 실행한다.

### Spring Data JPA

JPA + Hibernate를 더 쉽게 쓰도록 도와주는 Spring 프레임워크 라이브러리이다. 얘는 Repository 레이어 자동 생성, 메서드 이름으로 쿼리 자동 생성, 기본 CRUD 자동 제공(JpaRepository), JPQL 등을 제공한다. 즉, JPA + Hibernate를 손 안대고 쓰게 하는 레이어이다. 개발자는 interface로 구현된 클래스에 JpaRepository를 extends하여 사용하기만 하면 된다. 그렇게 하면 Spring Data JPA가 전부 알아서 추상화해준다.

# Persistence Context

JPA는 엔티티를 직접 DB에 넣지 않는다. 일단 “영속성 컨텍스트” 라는 메모리 공간에 저장해두고 관리한다. 즉, 영속성 컨텍스트는 엔티티를 영구 저장하기 위해 JPA가 관리하는 1차 캐시(메모리 저장소)이다.

1. First-level Cache : DB에서 조회한 엔티티는 영속성 컨텍스트 안의 캐시에 먼저 저장된다. 두 번째 호출은 DB 조회가 아니라 메모리에서 즉시 반환된다. 이로써 성능이 향상되고 불필요한 SELECT를 방지할 수 있다.
2. 동일성 보장 (Identity) : 같은 PK의 엔티티는 항상 같은 객체가 됨을 보장한다.
3. 변경 감지 (Dirty Checking) : 엔티티 필드 값을 바꾸기만 해도 자동으로 UPDATE SQL이 나간다. JPA는 트랜잭션이 끝날 때 엔티티 이전 스냅샷과 현재 엔티티 상태를 비교해서 달라진 부분만 UPDATE SQL로 만들어 DB에 반영한다. 즉, 개발자는 SQL 없이 객체 값만 바꾸면 되므로 유지보수, 생산성이 압도적으로 좋아지는 효과를 불러온다.
4. 쓰기 지연 (Write Delay) : persist() 해도 즉시 insert 쿼리를 실행하지 않는다. 트랜잭션 커밋 후 flush 하고 그 후 DB 반영 순으로 이뤄진다. 여러 개의 INSERT SQL을 하나의 batch로 묶어 최적화한다.

### JPA의 엔티티 생명주기 4단계

1. 비영속 (new, transient) : 아직 JPA가 모르는 상태, 영속성 컨텍스트 밖, DB와 전혀 관련 없으며, persist() 하지 않으면 그냥 자바 객체와 다를 바 없다.

```java
Product p = new Product();
```

2. 영속 (managed) : JPA가 관리하는 상태, 1차 캐시 안에 저장된 상태이다.

```java
em.persist(p);
```

영속 상태가 되면 동일성 보장, Dirty Checking 등등이 적용되며 flush/commit 시 DB에 반영된다.

3. 준영속 (detached) : 영속성 컨텍스트에서 분리된 상태

```java
em.detach(p);
```

그냥 일반 자바 객체처럼 된다.

4. 삭제 (removed)

```java
em.remove(p);
```

flush() 시 delete SQL 생성됨. commit 시 삭제 확정

# Repository 계층

### @Entity

도메인 계층(모델 계층)에서 사용함.

도메인 클래스 = DB 테이블이라고 선언하는 것이다.

```java
@Entity
public class Product {
	@Id
	private Long id;
	private String name;
}
```

JPA가 관리하는 객체가 되며(영속성 컨텍스트가 다루는 객체라는 의미와 동일), DB 테이블의 한 ROW를 자바 객체로 표현한 것이다. 필드는 DB 컬럼에 매핑되며 반드시 기본 생성자가 필요하다. 기본 생성자를 만들기 위해 @Id 가 사용되었다. 

Repository는 Entity를 기반으로 CRUD를 수행한다. 즉, Repository는 Entity를 DB와 연결하는 통로와 같다.

### @Repository

엔티티를 DB와 연결하는 데이터 접근 계층(DAO)이다.

1. 엔티티 저장/조회/삭제 등의 CRUD 담당

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
}
```

2. 예외 변환 (Exception Translation) : @Repository가 붙으면 JPA/Hibernate가 던지는 예외를 Spring의 DataAccessException으로 변환해준다.
3. Spring Bean 등록 : 스프링 컨테이너에 의해 자동 빈 등록, 주입 가능

### @Transactional

이 애너테이션은 서비스 계층에서 주로 사용한다. 서비스 로직이 비즈니스 로직의 단위이며 트랜잭션은 비즈니스 작업의 묶음을 의미하기 때문이다.

1. 메서드를 하나의 트랜잭션 단위로 묶는다.

```java
@Transactional
public void saveProduct(Product p) {
	productRepository.save(p);
}
```

DB 작업은 반드시 트랜잭션 안에서 이루어지므로 Spring이 이 애너테이션을 보고 트랜잭션 시작-커밋을 자동 수행한다.

2. 트랜잭션이 시작되면 영속성 컨텍스트가 생성되고, persist(), find() 등이 이 컨텍스트 안에서 실행되며 트랜잭션 커밋 시 flush() 되어 DB에 반영까지 된다.
3. 읽기 전용 트랜잭션 또한 가능하다. 조회만 하는 경우

```java
@Transactional(readOnly = true)
public Product find(Long id) { ... }
```

dirty checking이 비활성화 되어 성능이 향상되고 flush가 불필요해진다.

### @Transaction을 줄여 쓰자?

Spring이 보장하는 것은 비즈니스 로직의 Atomicity이다. 하나라도 실패하면 전체 rollback. 하지만 Spring 자체는 DB가 아니다. DB의 atomicity를 대신해줄 수 없다. 따라서 우리는 @Transactional의 도움을 받아 이를 구현하곤 한다. 즉, 이 애너테이션을 사용하는 경우는 어떠한 DB Update가 하나의 트랜잭션 안에서 원자적으로 처리돼야 할 때만 필요하다.

1. 조회만 한다면?
    1. 필요 없다. 데이터를 읽기만 하니까 rollback/commit 이런걸 할 이유가 없다. 사실 위에서 말한 readOnly = true 역시 굳이 필요 없다. 오히려 readOnly 불러오느라 DB로 쿼리가 추가로 6개가 더 날아가는 불필요함이 있다. 네트워크 트래픽이 6배 증가하여 성능이 떨어질 수 있다.
2. 단일 row update
    1. 필요 없다. DB는 단일 row update는 이미 원자적으로 처리해준다.
3. 동시성 제어만 필요한 경우
    1. Transactional로 해결할 수 있는 문제가 아니다. 이것만으로는 Optimistic, Pessimistic Lock 같은 동시성 제어를 완전히 해결할 수 없다.
4. JPA 기본 Repository는 이미 내부적으로 @Transactional이 붙어 있다. 이미 클래스 래벨에 다 붙어있고 update 메서드에도 다 붙어있다. 그런데 개발자는 무의식적으로 또 다시 트랜잭션을 사용하게 된다.

최소한 이럴 때만 사용하자.

1. 여러 Row를 업데이트해야 할 때
2. 여러 Repository를 묶어야 할 때
3. 비즈니스 로직이 원자성을 가지는 것이 매우 중요할 때
4. JPA의 Dirty Checking을 무조건 확인해야 할 때
