package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.MessageDto;
import java.util.List;

public interface MessageService {
    List<MessageDto> getAll();
    MessageDto getById(Long id);
    MessageDto create(MessageDto dto);
    MessageDto update(Long id, MessageDto dto);
    void delete(Long id);
}
