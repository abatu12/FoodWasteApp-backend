package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dto.MessageDto;
import com.example.foodwasteapp.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<MessageDto> getAllMessages() {
        return messageService.getAll();
    }

    @GetMapping("/{id}")
    public MessageDto getMessageById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    @PostMapping
    public MessageDto createMessage(@RequestBody MessageDto dto) {
        return messageService.create(dto);
    }

    @PutMapping("/{id}")
    public MessageDto updateMessage(
            @PathVariable Long id,
            @RequestBody MessageDto dto
    ) {
        return messageService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.delete(id);
    }
}
