package com.emergent.socialmedia.photosharing;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.resources.MediaResource;
import com.emergent.socialmedia.photosharing.service.MediaService;
import com.emergent.socialmedia.photosharing.service.ThirdPartyJWTVerifierInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MediaResource.class, secure = false)
public class MediaResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaService mediaService;


    @MockBean
    private ThirdPartyJWTVerifierInterceptor thirdPartyJWTVerifierInterceptor;

    User mockUser1 = new User();
    User mockUser2 = new User();
    Media mockMedia1 = new Media();
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockUser1.setId(1L);
        mockUser1.setFullName("Ashish Mahamuni");
        mockUser1.setEmail("ashish@test.com");

        mockUser2.setId(2L);
        mockUser2.setFullName("Mangesh Kshirsagar");
        mockUser2.setEmail("mangesh@test.com");

        mockMedia1.setId(1L);
        mockMedia1.setDownloadURI("mock download url");
        mockMedia1.setFileName("mock.jpg");
        mockMedia1.setUser(mockUser1);
    }

    @Test
    public void verifySuccessFileUpload() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.multipart(MediaResource.REST_USERID_ENDPOINT_PREFIX+MediaResource.REST_MEDIA,
                mockUser1.getId()).file("file","Content".getBytes()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void verifySuccessGetFeeds() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(MediaResource.REST_USERID_ENDPOINT_PREFIX+MediaResource.REST_GET_FEED_ENDPOINT,
                mockUser1.getId()).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
