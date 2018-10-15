package com.emergent.socialmedia.photosharing;

import com.emergent.socialmedia.photosharing.domain.Media;
import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.repositories.CommentsRepository;
import com.emergent.socialmedia.photosharing.repositories.LikesRepository;
import com.emergent.socialmedia.photosharing.repositories.MediaRepository;
import com.emergent.socialmedia.photosharing.repositories.UserRepository;
import com.emergent.socialmedia.photosharing.resources.dto.response.MediaResponseDTO;
import com.emergent.socialmedia.photosharing.resources.dto.response.ResponseContainerDTO;
import com.emergent.socialmedia.photosharing.service.MediaServiceImpl;
import com.emergent.socialmedia.photosharing.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MediaServiceTest {
    @InjectMocks
    private MediaServiceImpl mediaService;

    @MockBean
    private StorageService storageService;

    @MockBean
    private MediaRepository mediaRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LikesRepository likesRepository;

    @MockBean
    private CommentsRepository commentsRepository;

    ServletUriComponentsBuilder servletUriComponentsBuilder = Mockito.mock(ServletUriComponentsBuilder.class);

    @Before
    public void setup(){
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
        mockMedia1.setCreatedAt(new Date());
        mockMedia1.setUser(mockUser1);

        mockMedia2.setId(2L);
        mockMedia2.setDownloadURI("mock download url 2");
        mockMedia2.setFileName("mock2.jpg");
        mockMedia2.setCreatedAt(new Date());
        mockMedia2.setUser(mockUser2);

        mediaList = new ArrayList<>();
        mediaList.add(mockMedia1);
        mediaList.add(mockMedia2);
        when(mediaRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mediaList);
        when(userRepository.findById(mockUser1.getId())).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(mockUser2.getId())).thenReturn(Optional.of(mockUser2));
        when(mediaRepository.findById(mockMedia1.getId())).thenReturn(Optional.of(mockMedia1));
        when(mediaRepository.findById(mockMedia2.getId())).thenReturn(Optional.of(mockMedia2));

        Resource resource = new FileSystemResource(mockMedia1.getFileName());
        when(storageService.loadAsResource(String.valueOf(mockMedia1.getId()))).thenReturn(resource);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    private User mockUser1 = new User();
    private User mockUser2 = new User();
    private Media mockMedia1 = new Media();
    private Media mockMedia2 = new Media();
    private List<Media> mediaList = new ArrayList<>();

    @Test
    public void testSuccessfulStoreMedia(){
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock1.jpg", "content".getBytes());

        MediaResponseDTO MediaResponseDTO =
                mediaService.storeMedia(mockUser1.getId(),mockMultipartFile);
        assertEquals(mockMultipartFile.getOriginalFilename(), MediaResponseDTO.getFileName());
    }

    @Test
    public void testSuccessfulGetMedia(){

        Resource resource = mediaService.getMedia(mockMedia1.getId());

        assertEquals(mockMedia1.getFileName(), resource.getFilename());
    }

    @Test
    public void testSuccessfulGetFeed(){
        ResponseContainerDTO responseContainerDTO =
                mediaService.getAllMediaOrderByCreatedAtDesc
                        (mockUser1.getId(),
                                null,
                                null);
        assertEquals( 2, responseContainerDTO.getData().getChildren().size());
        assertEquals(mockMedia2.getId(), responseContainerDTO.getData().getAfter());
    }

    @Test
    public void testSuccessfulGetFeedWithAfterAndLimit(){
        Media mockMedia3 = new Media();
        mockMedia3.setId(3L);
        mockMedia3.setDownloadURI("mock download url 3");
        mockMedia3.setFileName("mock3.jpg");
        mockMedia3.setCreatedAt(new Date());
        mockMedia3.setUser(mockUser2);

        Media mockMedia4 = new Media();
        mockMedia4.setId(4L);
        mockMedia4.setDownloadURI("mock download url 4");
        mockMedia4.setFileName("mock4.jpg");
        mockMedia4.setCreatedAt(new Date());
        mockMedia4.setUser(mockUser2);

        Media mockMedia5 = new Media();
        mockMedia5.setId(5L);
        mockMedia5.setDownloadURI("mock download url 5");
        mockMedia5.setFileName("mock5.jpg");
        mockMedia5.setCreatedAt(new Date());
        mockMedia5.setUser(mockUser2);

        mediaList.add(mockMedia3);
        mediaList.add(mockMedia4);
        mediaList.add(mockMedia5);

        when(mediaRepository.findById(mockMedia3.getId())).thenReturn(Optional.of(mockMedia3));
        when(mediaRepository.findById(mockMedia3.getId())).thenReturn(Optional.of(mockMedia3));
        when(mediaRepository.findById(mockMedia3.getId())).thenReturn(Optional.of(mockMedia3));
        when(likesRepository.findAllByUserId(mockUser1.getId())).thenReturn(Optional.of(new ArrayList<>()));

        Pageable pageable = PageRequest.of(0, 2, new Sort(Sort.Direction.DESC, Media.MEDIA_ID));
        when(mediaRepository.findAllByCreatedAtBefore(mockMedia3.getCreatedAt(), pageable)).thenReturn(mediaList.subList(0,2));


        ResponseContainerDTO responseContainerDTO =
                mediaService.getAllMediaOrderByCreatedAtDesc
                        (mockUser1.getId(),
                                3L,
                                2);
        assertEquals(2,responseContainerDTO.getData().getChildren().size());
        assertEquals(mockMedia2.getId(), responseContainerDTO.getData().getAfter());
    }
}
