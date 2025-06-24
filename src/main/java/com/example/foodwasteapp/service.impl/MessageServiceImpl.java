package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.Message;
import com.example.foodwasteapp.dto.MessageDto;
import com.example.foodwasteapp.repository.MessageRepository;
import com.example.foodwasteapp.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepo;

    public MessageServiceImpl(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public List<MessageDto> getAll() {
        return messageRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public MessageDto getById(Long id) {
        Message m = messageRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        return toDto(m);
    }

    @Override
    public MessageDto create(MessageDto dto) {
        Message entity = toEntity(dto);
        Message saved = messageRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public MessageDto update(Long id, MessageDto dto) {
        Message existing = messageRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        existing.setSenderID(dto.getSenderID());
        existing.setReceiverID(dto.getReceiverID());
        existing.setListingID(dto.getListingID());
        existing.setText(dto.getText());
        existing.setTimestamp(dto.getTimestamp());
        Message updated = messageRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        messageRepo.deleteById(id);
    }

    private MessageDto toDto(Message m) {
        MessageDto dto = new MessageDto();
        dto.setId(m.getId());
        dto.setSenderID(m.getSenderID());
        dto.setReceiverID(m.getReceiverID());
        dto.setListingID(m.getListingID());
        dto.setText(m.getText());
        dto.setTimestamp(m.getTimestamp());
        return dto;
    }

    private Message toEntity(MessageDto dto) {
        Message m = new Message();
        m.setSenderID(dto.getSenderID());
        m.setReceiverID(dto.getReceiverID());
        m.setListingID(dto.getListingID());
        m.setText(dto.getText());
        m.setTimestamp(dto.getTimestamp());
        return m;
    }
}
