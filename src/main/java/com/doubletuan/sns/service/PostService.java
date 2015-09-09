package com.doubletuan.sns.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doubletuan.sns.config.Constants;
import com.doubletuan.sns.domain.PostImage;
import com.doubletuan.sns.domain.UserPost;
import com.doubletuan.sns.repository.PostImageRepository;
import com.doubletuan.sns.repository.UserPostRepository;

@Service("PostService")
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);
    
    @Inject
    private UserPostRepository userPostRepository;
    
    @Inject
    private PostImageRepository postImageRepository;
    
    public void createNewPost(UserPost userPost, MultipartFile[] images) throws IOException {
    	
    	if (userPost.getId() == null) {
			
			userPost.setGreetCount(0);
			userPost.setCommentsCount(0);
		}
    	
    	List<PostImage> postImages = new ArrayList<>();
    	
    	if (images != null && images.length > 0) {

			for (int i = 0; i < images.length; i++) {
				
				MultipartFile file = images[i];

				String imageFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
						
				FileOutputStream fileOutputStream = new FileOutputStream(new File(Constants.POST_IMAGE_PATH + imageFileName));

				IOUtils.copy(file.getInputStream(), fileOutputStream);
				
				String src = Constants.POST_IMAGE_RESOURCE_PATH + imageFileName;
				
				PostImage postImage = new PostImage();
				postImage.setSrc(src);
				
				postImages.add(postImage);
				
				postImage.setUserPost(userPost);
				postImageRepository.save(postImage);
			}
		}
    	
    	userPostRepository.save(userPost);
    	
    }
    
    public void saveSingleImageForPost(Long postId, MultipartFile file) throws IOException {
    	
    	UserPost userPost = userPostRepository.findOne(postId);
    	
    	String imageFileName = userPost.getJid() + System.currentTimeMillis() + "_" + file.getOriginalFilename();
		
		FileOutputStream fileOutputStream = new FileOutputStream(new File(Constants.POST_IMAGE_PATH + imageFileName));

		IOUtils.copy(file.getInputStream(), fileOutputStream);
		
		String src = Constants.POST_IMAGE_RESOURCE_PATH + imageFileName;
		
		PostImage postImage = new PostImage();
		postImage.setSrc(src);
		
		postImage.setUserPost(userPost);
		postImageRepository.save(postImage);
    }

}
