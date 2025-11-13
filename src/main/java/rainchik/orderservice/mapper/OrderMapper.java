package rainchik.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import rainchik.orderservice.dto.OrderDTO;
import rainchik.orderservice.dto.OrderResponseDTO;
import rainchik.orderservice.entity.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponseDTO toOrderResponseDTO(Order order);
    Order toOrder(OrderDTO orderDTO);
}
