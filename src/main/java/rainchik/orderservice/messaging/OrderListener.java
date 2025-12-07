package rainchik.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rainchik.orderservice.dto.OrderDTO;
import rainchik.orderservice.dto.PaymentResponseDTO;
import rainchik.orderservice.entity.Status;
import rainchik.orderservice.exception.OrderDoesNotExistException;
import rainchik.orderservice.service.OrderService;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = {"create-payment-topic"})
public class OrderListener {

    private final OrderService orderService;

    @KafkaHandler
    public void listen(PaymentResponseDTO dto) throws OrderDoesNotExistException {

        if(Objects.equals(dto.getPaymentStatus(), "SUCCESS")){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setStatus(Status.COMPLETED.name());
            orderService.updateOrder(dto.getOrderId(), orderDTO);
        }
        else {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setStatus(Status.FAILED.name());
            orderService.updateOrder(dto.getOrderId(), orderDTO);
        }

    }
}
