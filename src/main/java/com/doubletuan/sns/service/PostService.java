package com.doubletuan.sns.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doubletuan.sns.config.Constants;
import com.doubletuan.sns.domain.PostComment;
import com.doubletuan.sns.domain.PostImage;
import com.doubletuan.sns.domain.UserPost;
import com.doubletuan.sns.repository.PostCommentRepository;
import com.doubletuan.sns.repository.PostImageRepository;
import com.doubletuan.sns.repository.SystemConfigurationRepository;
import com.doubletuan.sns.repository.UserPostRepository;
import com.doubletuan.sns.web.rest.util.PaginationUtil;

@Service("PostService")
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);
    
    @Inject
    private UserPostRepository userPostRepository;
    
    @Inject
    private PostImageRepository postImageRepository;
    
    @Inject
	private SystemConfigurationRepository systemConfigurationRepository;
    
    @Inject
    private PostCommentRepository postCommentRepository;
    
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
    
    public PostImage saveSingleImageForPost(Long postId, MultipartFile file) throws IOException {
    	
    	UserPost userPost = userPostRepository.findOne(postId);
    	
    	String userJid = userPost.getJid() == null? "nobody" : userPost.getJid();
    	
    	String imageFileName = System.currentTimeMillis() + "_"+ UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    	String rootPath = systemConfigurationRepository.findConfigurationByIdentity(Constants.USER_UPLOADED_FILE_ROOT_PATH);
    	String userUploadedPostRelativePath = systemConfigurationRepository.findConfigurationByIdentity(Constants.USER_UPLOADED_FILE_RELATIVE_PATH);
		String fileName = rootPath + userUploadedPostRelativePath + userJid;
		
		log.debug("new saving file folder -> " + fileName);
		
    	File newSavedFile = new File(fileName);
    	
    	if (!newSavedFile.exists()) {
			newSavedFile.mkdir();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName + "/" + imageFileName));

		IOUtils.copy(file.getInputStream(), fileOutputStream);
		
		PostImage postImage = new PostImage();
		postImage.setSrc(imageFileName);
		
		postImage.setUserPost(userPost);
		PostImage savedPostImage = postImageRepository.save(postImage);
		
		String systemRootHttpPath = systemConfigurationRepository.findConfigurationByIdentity(Constants.SYSTEM_ROOT_HTTP_PATH);
		savedPostImage.setFullSrc(systemRootHttpPath + userUploadedPostRelativePath + userPost.getJid() + "/" + imageFileName);
		
		return savedPostImage;
    }
    
    public Page<UserPost> findUserReadablePosts(List<String> jids, Integer offset, Integer limit) {
    	
    	Page<UserPost> page = userPostRepository.findByJidInOrderByIdDesc(jids, PaginationUtil.generatePageRequest(offset, limit));
    	List<UserPost> posts = page.getContent();
    	
    	for (UserPost userPost : posts) {
			List<String> images = postImageRepository.findSrcByPostId(userPost.getId());
			
			if (images != null && images.size() > 0) {
				
				String systemRootHttpPath = systemConfigurationRepository.findConfigurationByIdentity(Constants.SYSTEM_ROOT_HTTP_PATH);
		    	String userUploadedPostRelativePath = systemConfigurationRepository.findConfigurationByIdentity(Constants.USER_UPLOADED_FILE_RELATIVE_PATH);
		    	
		    	List<String> newList = new ArrayList<>();
				for (String image : images) {
					newList.add(systemRootHttpPath + userUploadedPostRelativePath + userPost.getJid() + "/" + image);
				}
				
				userPost.setImageSrcList(newList);
			}
			
			List<PostComment> postComments = postCommentRepository.findByUserPost_Id(userPost.getId());
			if (postComments != null) {
				userPost.setCommentsCount(postComments.size());
				userPost.setCommentList(postComments);
			}
			
		}
    	
    	return page;
    	
    }
    
    public PostComment commentPost(PostComment postComment, Long postId) {
    	
    	UserPost userPost = userPostRepository.findOne(postId);
    	
    	postComment.setUserPost(userPost);
    	
    	PostComment savedPostComment = postCommentRepository.save(postComment);
    	
    	return savedPostComment;
    	
    }

}
