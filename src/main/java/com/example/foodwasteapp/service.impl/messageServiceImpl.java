package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.message;
import com.example.foodwasteapp.dto.messageDto;
import com.example.foodwasteapp.repository.messageRepository;
import com.example.foodwasteapp.service.messageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class messageServiceImpl implements messageService {

    private final messageRepository messageRepo;

    public messageServiceImpl(messageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public List<messageDto> getAll() {
        return messageRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public messageDto getById(Long id) {
        message m = messageRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        return toDto(m);
    }

    @Override
    public messageDto create(messageDto dto) {
        message entity = toEntity(dto);
        message saved = messageRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public messageDto update(Long id, messageDto dto) {
        message existing = messageRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        existing.setSenderID(dto.getSenderID());
        existing.setReceiverID(dto.getReceiverID());
        existing.setListingID(dto.getListingID());
        existing.setText(dto.getText());
        existing.setTimestamp(dto.getTimestamp());
        message updated = messageRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        messageRepo.deleteById(id);
    }

    private messageDto toDto(message m) {
        messageDto dto = new messageDto();
        dto.setId(m.getId());
        dto.setSenderID(m.getSenderID());
        dto.setReceiverID(m.getReceiverID());
        dto.setListingID(m.getListingID());
        dto.setText(m.getText());
        dto.setTimestamp(m.getTimestamp());
        return dto;
    }

    private message toEntity(messageDto dto) {
        message m = new message();
        m.setSenderID(dto.getSenderID());
        m.setReceiverID(dto.getReceiverID());
        m.setListingID(dto.getListingID());
        m.setText(dto.getText());
        m.setTimestamp(dto.getTimestamp());
        return m;
    }
}
