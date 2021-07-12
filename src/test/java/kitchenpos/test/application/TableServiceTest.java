package kitchenpos.test.application;

import kitchenposNew.menu.domain.Menu;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderLineItem;
import kitchenposNew.order.domain.OrderLineItems;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.order.exception.NotChangeToEmptyThatGroupTable;
import kitchenposNew.order.exception.NotFoundOrderTable;
import kitchenposNew.table.application.TableService;
import kitchenposNew.table.domain.OrderTable;
import kitchenposNew.table.domain.OrderTableRepository;
import kitchenposNew.table.dto.OrderTableRequest;
import kitchenposNew.table.dto.OrderTableResponse;
import kitchenposNew.table.exception.NotChangeNumberOfGuestThatEmptyTable;
import kitchenposNew.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("테이블 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private OrderTableRequest firstOrderTableRequest;
    private OrderTableRequest secondOrderTableRequest;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    public void setUp() {
        // given
        firstOrderTableRequest = 주문_테이블_생성(10);
        secondOrderTableRequest = 주문_테이블_생성(10);
    }

    @Test
    @DisplayName("테이블 등록 테스트")
    void 테이블_등록() {
        // when
        // 주문 테이블 등록 요청
        OrderTable orderTable = firstOrderTableRequest.toOrderTable();
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
        OrderTableResponse expected = tableService.create(firstOrderTableRequest);

        // then
        // 주문 테이블 저장
        assertThat(expected.getId()).isEqualTo(orderTable.getId());
        assertThat(expected.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 리스트 조회")
    void 테이블_리스트_조회() {
        // given
        // 주문 테이블 저장되어 있음
        OrderTable firstOrderTable = firstOrderTableRequest.toOrderTable();
        OrderTable secondOrderTable = firstOrderTableRequest.toOrderTable();

        // when
        // 주문 테이블 리스트 조회함
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        List<OrderTableResponse> expected = tableService.list();

        // then
        // 리스트 조회됨
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("저장되어 있지 않은 테이블 빈 테이블로 변경 시 예외 처리")
    void isBlackOrderTable_exception() {
        // when
        // 주문 테이블 리스트 조회
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(NotFoundOrderTable.class);
    }

    @Test
    @DisplayName("단체 테이블에 속한 테이블 변경 불가")
    void isOrderTalbleOfGroupTable_exception() {
        // give
        // 해당 주문 테이블은 단체 테이블임
        OrderTable firstOrderTable = firstOrderTableRequest.toOrderTable();
        firstOrderTable.changeTableGroupId(1L);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 계산 완료 테이블임
        order.changeOrderStatusComplete();

        // when
        // 주문 테이블 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));
        when(orderRepository.findByOrderTableId(1L)).thenReturn(Optional.of(order));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(NotChangeToEmptyThatGroupTable.class);
    }

    @Test
    @DisplayName("요리중이거나 식사중인 테이블 변경 불가")
    void isCookingOrMealingOrderTable_exception() {
        // give
        // 해당 주문 테이블 생성되어 있음
        OrderTable firstOrderTable = firstOrderTableRequest.toOrderTable();

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // when
        // 주문 테이블 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));
        when(orderRepository.findByOrderTableId(1L)).thenReturn(Optional.of(order));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(NotChangeToEmptyThatCookingOrMealTable.class);
    }

    @Test
    @DisplayName("주문 테이블 정상 변경")
    void 주문_테이블_정상_변경() {
        // give
        // 주문 테이블 등록되어 있음
        OrderTable firstOrderTable = firstOrderTableRequest.toOrderTable();

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 계산 완료 테이블
        order.changeOrderStatusComplete();
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));
        when(orderRepository.findByOrderTableId(1L)).thenReturn(Optional.of(order));

        //then 변경됨
        OrderTableResponse expected = tableService.changeEmpty(1L);
        assertThat(expected.getEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 손님 명수 0 이하 일때 오류")
    void isZeroGuestNumber_exception() {
        // given
        // 손님 명수 오류 등록
        firstOrderTableRequest = 주문_테이블_생성(-10);

        // and
        // 주문 테이블 등록되어 있음.
        OrderTable orderTable = new OrderTable(1L, 10);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블 예외처리")
    void isBlankOrderTable_exception() {
        // when
        // 등록되지 않은 테이블
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(NotFoundOrderTable.class);
    }

    @Test
    @DisplayName("빈 테이블 예외처리")
    void isEmptyOrderTable_exception() {
        // when
        // 빈 테이블임
        OrderTable orderTable = new OrderTable(1L, 10);
        orderTable.changeToEmpty();
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(NotChangeNumberOfGuestThatEmptyTable.class);
    }

    @Test
    @DisplayName("주문 테이블 인원수 정상 변경")
    void 주문_테이블_인원수_정상_변경() {
        // when
        // 비지 않은 테이블 생성
        OrderTable orderTable = new OrderTable(1L, 5);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.ofNullable(orderTable));

        // then
        // 정상 변경
        OrderTableResponse expected = tableService.changeNumberOfGuests(1L, firstOrderTableRequest);
        assertThat(expected.getNumberOfGuests()).isEqualTo(10);
    }

    private OrderTableRequest 주문_테이블_생성(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }
}