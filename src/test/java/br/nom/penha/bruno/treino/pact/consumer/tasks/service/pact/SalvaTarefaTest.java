package br.nom.penha.bruno.treino.pact.consumer.tasks.service.pact;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.nom.penha.bruno.treino.pact.consumer.tasks.model.Task;
import br.nom.penha.bruno.treino.pact.consumer.tasks.service.TasksConsumer;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SalvaTarefaTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "ConsumidorBasico")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){
        DslPart corpoRequisicao = new PactDslJsonBody()
                            .stringType("task", "Tarefa com data em String")
                            .date("dueDate", "yyyy-MM-dd", new Date());

        DslPart corpoResponse = new PactDslJsonBody()
                            .numberType("id")
                            .stringType("task")
                            .date("dueDate", "yyyy-MM-dd", new Date());

        return construtor
                .uponReceiving("Salva a data de uma tarefa com uma string")
                    .path("/todo")
                    .method("POST")
                    .body(corpoRequisicao)
                .willRespondWith()
                    .status(201)
                    .body(corpoResponse)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Preparo o ambiente
        TasksConsumer consumidor = new TasksConsumer(mockProvider.getUrl());
        System.out.println("mockProvider.getUrl() >>>>>>>>>>> " + mockProvider.getUrl());

        // Executo a tarefa que quero testar
        String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Task tarefa = consumidor.saveTask("Tarefa com data em String", dueDate);

        // Verifico o resultado
        assertThat(tarefa.getId(), is(notNullValue()));
        assertThat(tarefa.getTask(), is(notNullValue()));


    }
}