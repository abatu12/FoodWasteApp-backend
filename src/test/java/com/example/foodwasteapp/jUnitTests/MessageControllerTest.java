package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.dbmodel.Message;
import com.example.foodwasteapp.dto.MessageDto;
import com.example.foodwasteapp.repository.MessageRepository;
import com.example.foodwasteapp.service.MessageService;
import com.example.foodwasteapp.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    private MessageRepository messageRepository;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        messageService = new MessageServiceImpl(messageRepository);
    }

    private Message createMessageEntity(Long id) {
        Message m = new Message();
        m.setId(id);
        m.setSenderID(1L);
        m.setReceiverID(2L);
        m.setListingID(3L);
        m.setText("Test message");
        m.setTimestamp(LocalDateTime.now());
        return m;
    }

    private MessageDto createMessageDto() {
        MessageDto dto = new MessageDto();
        dto.setSenderID(1L);
        dto.setReceiverID(2L);
        dto.setListingID(3L);
        dto.setText("Test message");
        dto.setTimestamp(LocalDateTime.now());
        return dto;
    }

    @Test
    void getAll_ReturnsList() {
        when(messageRepository.findAll()).thenReturn(List.of(createMessageEntity(1L), createMessageEntity(2L)));

        List<MessageDto> messages = messageService.getAll();

        assertEquals(2, messages.size());
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ReturnsDto() {
        Message msg = createMessageEntity(1L);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(msg));

        MessageDto dto = messageService.getById(1L);

        assertNotNull(dto);
        assertEquals(msg.getText(), dto.getText());
    }

    @Test
    void getById_NotFound_Throws() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> messageService.getById(1L));
    }

    @Test
    void create_SavesAndReturnsDto() {
        MessageDto dto = createMessageDto();
        Message savedEntity = createMessageEntity(1L);
        when(messageRepository.save(any(Message.class))).thenReturn(savedEntity);

        MessageDto created = messageService.create(dto);

        assertNotNull(created);
        assertEquals(savedEntity.getId(), created.getId());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void update_ExistingId_UpdatesAndReturnsDto() {
        Message existing = createMessageEntity(1L);
        MessageDto updateDto = createMessageDto();
        updateDto.setText("Updated text");

        when(messageRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));

        MessageDto updated = messageService.update(1L, updateDto);

        assertEquals("Updated text", updated.getText());
        verify(messageRepository).findById(1L);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void update_NotFound_Throws() {
        MessageDto dto = createMessageDto();
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> messageService.update(1L, dto));
    }

    @Test
    void delete_CallsRepository() {
        doNothing().when(messageRepository).deleteById(1L);

        messageService.delete(1L);

        verify(messageRepository, times(1)).deleteById(1L);
    }
}
