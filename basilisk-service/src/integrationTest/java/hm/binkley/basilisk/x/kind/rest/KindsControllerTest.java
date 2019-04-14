package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.TestJson.readTestJsonRequest;
import static hm.binkley.basilisk.TestJson.readTestJsonResponse;
import static hm.binkley.basilisk.x.TestFixtures.fixedKind;
import static hm.binkley.basilisk.x.TestFixtures.fixedNear;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@JsonWebMvcTest(KindsController.class)
class KindsControllerTest {
    private final MockMvc controller;

    @MockBean
    private Kinds kinds;
    @MockBean
    private Nears nears;

    private Near near;
    private Kind kind;
    private String kindCode;
    private BigDecimal kindCoolness;

    @BeforeEach
    void setUp() {
        near = fixedNear();
        final var nearCode = near.getCode();
        near = spy(near);
        kind = fixedKind(nears);
        kindCode = kind.getCode();
        kindCoolness = kind.getCoolness();
        kind = spy(kind);

        lenient().doReturn(Optional.of(near))
                .when(nears).byCode(nearCode);
        lenient().doReturn(Optional.of(kind))
                .when(kinds).byCode(kindCode);
    }

    @Test
    void shouldGetNone()
            throws Exception {
        when(kinds.all()).thenReturn(Stream.empty());

        controller.perform(get("/kinds/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        kind.addNear(near);
        when(kinds.all()).thenReturn(Stream.of(kind));

        controller.perform(get("/kinds/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldGetOne()
            throws Exception {
        kind.addNear(near);

        controller.perform(get("/kinds/get/" + kindCode))
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldPostOne()
            throws Exception {
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);

        controller.perform(post("/kinds/post")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/kinds/get/" + kindCode))
                .andExpect(content().json(readTestJsonResponse(), true));

        verify(kind).save();
    }

    @Test
    void shouldPutOneNew()
            throws Exception {
        doReturn(Optional.empty()).when(kinds).byCode(kindCode);
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);
        doReturn(kind).when(kind).save();

        controller.perform(put("/kinds/put/" + kindCode)
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(content().json(readTestJsonResponse(), true));

        verify(kind).save();
    }

    @Test
    void shouldPutOneExisting()
            throws Exception {
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);
        doReturn(kind).when(kind).save();

        controller.perform(put("/kinds/put/" + kindCode)
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));

        verify(kind).save();
    }

    @Test
    void shouldPostNearsAdd()
            throws Exception {
        doReturn(kind).when(kind).save();

        controller.perform(post("/kinds/post/" + kindCode + "/nears/add")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));

        verify(kind).save();
    }

    @Test
    void shouldPostNearsRemove()
            throws Exception {
        kind.addNear(near);
        doReturn(kind).when(kind).save();

        controller.perform(post("/kinds/post/" + kindCode + "/nears/remove")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isOk())
                .andExpect(content().json(readTestJsonResponse(), true));

        verify(kind).save();
    }

    @Test
    void shouldDeleteOne()
            throws Exception {
        doReturn(kind).when(kind).delete();

        controller.perform(delete("/kinds/delete/" + kindCode))
                .andExpect(status().isNoContent());

        verify(kind).delete();
    }
}
