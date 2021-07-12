package kitchenposNew.table.dto;

import kitchenposNew.table.domain.OrderTable;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable toOrderTable(){
        return new OrderTable(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public Long getId(){
        return this.id;
    }
}