package com.algaworks.algamoney.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.algaworks.algamoney.api.model.Categoria;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest // indica que o spring boot deve procurar por uma class anotada com @springbootapplication p/ iniciar app
@AutoConfigureMockMvc // configura ambiente de testes para iniciar o contexto todo, sem subir o servidor, p/ testar os recursos do app
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlgamoneyApiResourceCategoriaTests {

	@Autowired
	private MockMvc mockMvc;
	private Long codigoRegistroTeste = 0L;

	/**
	 * Verificar post com body vazio, deve retornar 404 + mensagem com id do erro
	 * @throws Exception
	 */
    @Test
    public void a_criarCatgoriaPostVazio () throws Exception {
        mockMvc.perform(post("/categorias").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").isNotEmpty())
                .andExpect(jsonPath("$.errorId").isNotEmpty())
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Verificar post com body inválido, deve retornar 404 + mensagem com id erro
     *
     * @throws Exception
     */
    @Test
    public void b_criarCategoriaAtributoInvalido () throws Exception {
        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\" : \"teste\", \"chuchu\" : \"abc\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").isNotEmpty())
                .andExpect(jsonPath("$.errorId").isNotEmpty())
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Verificar post com atributo obrigatório vazio, deve retornar 404 + mensagem com id erro
     *
     * @throws Exception
     */
    @Test
    public void c_criarCategoriaAtributoObrigatorioVazio () throws Exception {
        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\" : \"\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").isNotEmpty())
                .andExpect(jsonPath("$.errorId").isNotEmpty())
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Verificar post válido - deve retornar 201 created + novo registro com id > 0
     *
     * @throws Exception
     */
    @Test
    public void d_criarCategoria () throws Exception {
        MvcResult result = mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\" : \"registro teste\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codigo").exists())
                .andExpect(jsonPath("$.codigo").isNumber())
                .andExpect(jsonPath("$.codigo").isNotEmpty())
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.nome").isString())
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        List<Categoria> entities = getEntities(result);
        assertThat(entities).isNotNull().isNotEmpty();

        codigoRegistroTeste = entities.get(0).getCodigo();
    }

    /**
     * Verificar se retorna lista de registros
     *
     * @throws Exception
     */
    @Test
    public void e_listarCategorias () throws Exception {
        MvcResult result = mockMvc.perform(get("/categorias").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Categoria> entities = getEntities(result);

        assertThat(entities).isNotNull().isNotEmpty();
    }

    /**
     * Verificar se retorna registro específico
     *
     * @throws Exception
     */
    @Test
    public void f_listarCategoriaEspecifica () throws Exception {
        MvcResult result = mockMvc
                .perform(get("/categorias/{codigo}", codigoRegistroTeste).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Categoria> entities = getEntities(result);
        assertThat(entities).isNotNull().isNotEmpty().hasSize(1);
        assertThat(entities.get(0).getCodigo()).isEqualTo(codigoRegistroTeste);
    }

    /**
     * Verificar se retorna status correto qdo informado id inexistente
     *
     * @throws Exception
     */
    @Test
    public void g_listarCategoriaInexistente () throws Exception {
        mockMvc.perform(get("/categorias/{codigo}", -1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Verificar se exclui registro
     *
     * @throws Exception
     */
    @Test
    public void h_removeCategoria () throws Exception {
        mockMvc.perform(delete("/categorias/{codigo}", codigoRegistroTeste).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        mockMvc.perform(get("/categorias/{codigo}", codigoRegistroTeste).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Verificar status p/ exclusão registro inexistente
     *
     * @throws Exception
     */
    @Test
    public void i_removeCategoriaInexistente () throws Exception {
        mockMvc.perform(delete("/categorias/{codigo}", -1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Método utilitário para retornar coleção de instâncias da entidade utilizada no recurso em teste
     *
     * @param result
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private List<Categoria> getEntities (MvcResult result) throws Exception, UnsupportedEncodingException, IOException {
        if (result == null) {
            return null;
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsonContent = jsonMapper.readTree(result.getResponse().getContentAsString());

        if (jsonContent.isNull() || jsonContent.size() <= 0) {
            return null;
        }

        List<Categoria> entities = new ArrayList<Categoria>();

        if (jsonContent.isArray()) {
            for (JsonNode jsonNode : jsonContent) {
                Categoria categoria = new Categoria();
                categoria.setCodigo(jsonNode.get("codigo").asLong());
                categoria.setNome(jsonNode.get("nome").asText());

                entities.add(categoria);
            }
        } else {
            Categoria categoria = new Categoria();
            categoria.setCodigo(jsonContent.get("codigo").asLong());
            categoria.setNome(jsonContent.get("nome").asText());

            entities.add(categoria);
        }

        return entities.isEmpty() ? null : entities;
    }
}
