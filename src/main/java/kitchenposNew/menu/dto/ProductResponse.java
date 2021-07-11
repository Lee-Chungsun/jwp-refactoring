package kitchenposNew.menu.dto;

import kitchenposNew.menu.domain.MenuGroup;
import kitchenposNew.menu.domain.Product;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductResponse {
    public Long id;
    public String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}