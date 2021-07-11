# 키친포스

---
## 1단계 - 테스트를 통한 코드 보호
### 1단계 요구사항

-[ ] ```kichenpos``` package를 참고하여 요구사항 정리  
    * [마크다운](https://dooray.com/htmls/guides/markdown_ko_KR.html) 을 활용  
        -[ ] ```TableRestController.java```  
        -[ ] ```TableGroupRestController.java```  
        -[ ] ```ProductRestController.java```  
        -[ ] ```OrderRestController.java```  
        -[ ] ```MenuRestController.java```  
        -[ ] ```MenuGroupRestController.java```  
* 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성.  
* ```@SpringBootTest```를 이용한 통합 테스트 코드 또는 ```@ExtendWith(MockitoExtension.class)```를 이용한 단위 테스트 코드를 작성
---
## 요구 사항

### 메뉴 그룹 
  - [x] 메뉴 그룹을 등록할 수 있다.
    - [x] 메뉴 그룹 : ```그룹ID``` 와 ```그룹명```을 가지고 있다.
  - [x] 메뉴 그룹 리스트를 조회할 수 있다.

### 메뉴
  - [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴 : ```메뉴ID``` 와 ```메뉴명```, ```상품 총 가격```, ```메뉴에 포함된 상품 리스트```를 가지고 있다.
    - [x] 메뉴 가격의 유효성을 체크한다.
      - [x] 올바른 가격을 입력해야 하고 0원 이상이어야 한다.
      - [x] 메뉴에 포함된 모든 상품들은 존재 해야한다.
      - [x] 메뉴의 가격은 메뉴에 포함된 모든 상품들의 가격과 같거나 작아야 한다.
  - [x] 메뉴에 포함된 상품들을 모두 포함한 메뉴 리스트를 조회할 수 있다.
### 상품
  - [x] 상품을 등록할 수 있다.
    - [x] 상품 : ```상품ID``` 와 ```상품명```, ```상품 가격```을 가지고 있다.
    - [x] 상품 가격의 유효성을 체크한다.
      - [x] 올바른 가격을 입력해야 하고 0원 이상이어야 한다.
  - [x] 모든 상품의 리스트를 조회할 수 있다.

### 주문
  - [x] 주문을 등록할 수 있따.
    - [x] 주문 : ```주문ID``` 와 ```주문테이블ID```, ```주문 상태```, ```주문 시간```, ```주문 리스트```을 가지고 있다.
    - [x] 주문 리스트 : ```주문 순서```와 ```주문ID```, ```메뉴ID```, ```메뉴 수량``` 을 가지고 있다.
    - [x] 주문은 주문 리스트가 하나라도 존재해야 한다.
    - [x] 주문 리스트의 메뉴가 실제 등록된 메뉴들인지 체크한다.
    - [x] 주문 테이블이 실제 등록된 테이블인지 체크한다.
  - [x] 주문 리스트를 조회할 수 있다.

### 단체(그룹) 테이블
  - [x] 주문 테이블들을 단체(그룹)으로 등록할 수 있다.
    - [x] 단체(그룹) 테이블 : ```그룹 테이블ID``` 와 ```생성 시간```, ```주문한 테이블 목록``` 을 가지고 있다.
    - [x] 주문한 테이블 : ```테이블ID``` 와 ```속한 그룹테이블ID```, ```손님 수```, ```주문 가능한 테이블 여부``` 를 가지고 있다.
    - [x] 단체(그룹) 테이블은 주문한 테이블이 2 테이블 이상이어야 한다.
    - [x] 테이블들은 실제 등록된 테이블들 이어야 한다.
    - [x] 테이블은 이미 다른 단체(그룹) 테이블에 등록이 되어있으면 안된다.
  - [x] 단체(그룹) 테이블을 삭제할 수 있다.
    - [x] 요리중이거나 식사중이면 삭제할 수 없다.

### 주문 테이블
  - [x] 주문 테이블을 등록할 수 있따.
    - [x] 주문 테이블 : ```주문 테이블ID```와 ```그룹 테이블ID```, ```손님 수```, ```빈 자리인지 여부```를 가지고 있다.
  - [x] 모든 주문 테이블을 조회할 수 있다
  - [x] 주문 테이블을 빈 자리로 만들 수 있따.
    - [x] 단체(그룹) 테이블에 속해있으면 안된다.
    - [x] 요리중이거나 식사중이면 삭제할 수 없다.
  - [x] 손님 수를 수정할 수 있다.
    - [x] 0명 이상이어야 한다.
    - [x] 주문 테이블이 빈테이블이면 안된다.



### Entity Relation Diagram

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://github.com/Lee-Chungsun/jwp-refactoring/blob/step1/class-diagram.puml)


## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

---

## 2 단계 - 서비스 리팩터링

### 요구사항
* Spring Data JPA 사용 시 spring.jpa.hibernate.ddl-auto=validate 옵션을 필수로 준다.
* 데이터베이스 스키마 변경 및 마이그레이션이 필요하다면 아래 문서를 적극 활용한다.
    * [DB도 형상관리를 해보자!](https://meetup.toast.com/posts/173)
    
### 구현 list
- [x] Spring Data jpa 의존성 추가
- [x] menu service refactoring
    - [ ] menuProduct relation refactoring
- [x] menuGroup service refactoring
- [x] product service refactoring
- [ ] table service refactoring
- [ ] tableGroup service refactoring
- [ ] order service refactoring
