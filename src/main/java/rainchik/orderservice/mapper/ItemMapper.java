package rainchik.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import rainchik.orderservice.dto.ItemDTO;
import rainchik.orderservice.dto.ItemResponseDTO;
import rainchik.orderservice.entity.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    ItemResponseDTO toItemResponseDTO(Item item);
    Item toItem(ItemDTO itemDTO);

}
