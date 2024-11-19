package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional
public class LogisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String adminToken;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void testGetLogisticsCompanyGivenWrongBillCode() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics")
                        .header("authorization", adminToken)
                        .param("billCode", "9999999999")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testGetLogisticsCompany() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics")
                        .header("authorization", adminToken)
                        .param("billCode", "1234567890")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }
}
