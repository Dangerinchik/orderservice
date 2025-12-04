package rainchik.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import rainchik.orderservice.dto.OrderItemDTO;
import rainchik.orderservice.dto.OrderItemResponseDTO;
import rainchik.orderservice.entity.OrderItem;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);

}
