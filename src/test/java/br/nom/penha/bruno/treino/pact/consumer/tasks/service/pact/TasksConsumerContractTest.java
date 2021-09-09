package br.nom.penha.bruno.treino.pact.consumer.tasks.service.pact;

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TasksConsumerContractTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tarefas", this);

    @Pact(consumer = "ConsumidorBasico")
    public RequestResponsePact criaPacto(PactDslWithProvider construtor){
        return construtor
                .given("Existe uma tarefa com o id = 1")
                .uponReceiving("Quando vier uma tarefa #1")
                    .path("/todo/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body("{\n" +
                            "    \"id\": 1,\n" +
                            "    \"task\" : \"Tarefa do Pact\",\n" +
                            "    \"dueDate\": \"2021-09-09\"    \n" +
                            "}")
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Preparo o ambiente
        TasksConsumer consumidor = new TasksConsumer(mockProvider.getUrl());
        System.out.println("mockProvider.getUrl() >>>>>>>>>>> " + mockProvider.getUrl());

        // Executo a tarefa que quero testar
        Task tarefa = consumidor.getTask(1L);

        // Verifico o resultado
        assertThat(tarefa.getId(), is(1L));
        assertThat(tarefa.getTask(), is("Tarefa do Pact"));

    }
}