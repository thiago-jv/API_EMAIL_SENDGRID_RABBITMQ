package com.email.domain.service;


import com.email.domain.model.LancamentoDTO;
import com.email.domain.service.EnvioEmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class ApartamentoConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnvioEmailService envioEmailService;

    @SneakyThrows
    @RabbitListener(queues = {"${queue.name}"})
    public void consumer(@Payload Message payload) {
        var lancamentoDTO = objectMapper.readValue(payload.getBody(), LancamentoDTO.class);
        log.info("Payload recebido no ms-email" + lancamentoDTO);

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto("Novo aluguel gerado  "+lancamentoDTO.getIdLancamento())
                .corpo("Inquilino: "+lancamentoDTO.getNome()+ "/n" +
                       "Apartamento: "+lancamentoDTO.getNumeroQuarto()+ "/n" +
                        "Predio: "+lancamentoDTO.getPredio()+ "/n" +
                        "Data Entrada: "+lancamentoDTO.getDataEntrada()).destinatarios(Collections.singleton(lancamentoDTO.getEmail())).build();
        try {
          envioEmailService.enviar(mensagem);
            log.info("Emanil enviado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
