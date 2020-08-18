package by.shop.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table (name = "product")
public class Product {

    @Id
//    uuid - for custom uuid generation; uuid2 - RFC4122 compliant uuid
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id;

    @Column
    String name;

    @Column(name = "product_number")
    String productNumber;

    @Column(name ="serial_number" )
    String serialNumber;

    @Column(name = "produce_date")
    Date producedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                name.equals(product.name) &&
                productNumber.equals(product.productNumber) &&
                serialNumber.equals(product.serialNumber) &&
                producedDate.equals(product.producedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, productNumber, serialNumber, producedDate);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productNumber='" + productNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", producedDate=" + producedDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }
}
