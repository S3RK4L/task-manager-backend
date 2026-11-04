package uk.gov.hmcts.reform.dev.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.TaskStatus;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRepository taskRepository;

    @Test
    void createTaskReturns201() throws Exception {
        Task saved = new Task("Review case file", "Check the bundle",
                TaskStatus.TODO, LocalDateTime.of(2026, 11, 10, 9, 0));

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title":"Review case file","status":"TODO","dueDate":"2026-11-10T09:00:00"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Review case file"));
    }

    @Test
    void getTaskByIdReturns200() throws Exception {
        Task task = new Task("Review case file", "Check the bundle",
                TaskStatus.TODO, LocalDateTime.of(2026, 11, 10, 9, 0));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Review case file"));
    }

    @Test
    void getMissingTaskReturns404() throws Exception {
        when(taskRepository.findById(9999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasksReturns200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void createTaskMissingTitleReturns400() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status":"TODO","dueDate":"2026-11-10T09:00:00"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTaskStatusReturns200() throws Exception {
        Task task = new Task("Review case file", "Check the bundle",
                TaskStatus.TODO, LocalDateTime.of(2026, 11, 10, 9, 0));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status":"IN_PROGRESS"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateTaskInvalidStatusReturns400() throws Exception {
        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"status":"INVALID_STATUS"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTaskReturns204() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMissingTaskReturns404() throws Exception {
        when(taskRepository.findById(9999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().isNotFound());
    }
}