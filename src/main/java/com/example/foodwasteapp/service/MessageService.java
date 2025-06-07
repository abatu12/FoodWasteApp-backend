package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.messageDto;
import java.util.List;

public interface MessageService {
    List<messageDto> getAll();
    messageDto getById(Long id);
    messageDto create(messageDto dto);
    messageDto update(Long id, messageDto dto);
    void delete(Long id);
}
