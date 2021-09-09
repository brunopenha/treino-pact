package br.nom.penha.bruno.treino.pact.consumer.tasks.service;

import br.nom.penha.bruno.treino.pact.consumer.tasks.model.Task;
import br.nom.penha.bruno.treino.pact.consumer.utils.RequestHelper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TasksConsumerTest {

    private static final String URL_INVALIDA = "http://nao_deve_funcionar.com";

    @InjectMocks
    private TasksConsumer consumidor = new TasksConsumer(URL_INVALIDA);

    @Mock
    private RequestHelper auxiliar;

    @Test
    public void devePegarUmaTarefaExistente() throws IOException {
        // Arrumar o ambiente
        Map<String, String> tarefaExperada = new HashMap<>();
        tarefaExperada.put("id","1");
        tarefaExperada.put("task","Tarefa simulada");
        tarefaExperada.put("dueDate","2021-09-09");

        when(auxiliar.get(URL_INVALIDA + "/todo/1"))
                .thenReturn(tarefaExperada);

        // Excecução da tarefa que quero testar
        Task tarefa = consumidor.getTask(1L);

        // Verificação - a ação que executei foi da forma esperada?
        assertNotNull(tarefa);
        assertThat(tarefa.getId(),CoreMatchers.is(1L));

    }


}