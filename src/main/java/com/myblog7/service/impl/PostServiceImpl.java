package com.myblog7.service.impl;

import com.myblog7.entity.Post;
import com.myblog7.exception.ResourceNotFound;
import com.myblog7.payload.PostDto;
import com.myblog7.payload.PostResponse;
import com.myblog7.repository.PostRepository;
import com.myblog7.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service                                               //This service layer takes the Dto and converts to entity layer an saves the data
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
//@Component is a stero type @Service annotations is a sterio type in leyman terms sterio means to handling over the particular class to spring IOC , spring IOC will create a bean of it and maintain life cycle of the bean.
//BELOW modelmapper comes from the library this class object we cannot create using @Component.this class objet is to be created and this class object lifecycle is maintain we recquire bean.
   //@Bean can be used only in configuration files
    private ModelMapper modelMapper; //This modelMapper library is an external library , there are 2 types of library builtin external for ex repository is a built in future of spring boot.
//when I used @Autowired , automatically object is created and it's address is injected to the repository layer spring IOC knows which object is to be created bcz that is buildin
 //ModelMapper is not a builtin library now if i want here dependency injection to happen spring IOC will will complain cannot create a bean for it bcz it don't have any knowledge about which bean to create
  //why this is happening whenever external library is added and that library object u try to create with dependency injection it will not happen, like modelmapper repository layer object will get created bcz spring IOC already has that logic and information which obj is to create
    //But in case of modelmapper springIOC doesn't know which object to create bcz it's an external library which we added.
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Post post =  mapToEntity(postDto);
//        Post post = new Post(); //as we can't save the dto . bcz dto will never go to the database . so here we have to convert dto to entity object
//        //therefore we are creating the entity object
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        postRepository.save(post);
        Post savePost = postRepository.save(post);//What ever is saving in the database that's saved content is put that in the saved content


        PostDto dto =  mapToDto(savePost);
        return dto;
//        PostDto dto = new PostDto(); //we are creating dto object to copy the data from entity object.bcz service layer will always return back the dto, it will never return back the entity
//       //now service layer is giving dto back to controller and controller will give dto back to postman
//        dto.setId(savePost.getId());
//        dto.setTitle(savePost.getTitle());//After saving it returns back the saved entity ,, the saved entity is convert into Dto and the service layer
//        dto.setDescription(savePost.getDescription());//returns back the Dto to controller layer,now the controller layer will uses Response entity where it returns back the Dto and the status code as response.
//        dto.setContent(savePost.getContent());
//        return dto;
    }

    @Override
    public void deletePost(long id) { postRepository.deleteById(id);

    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Post not found with id:"+id)
        );// with this id post may be found or may not be found
   post.setTitle(postDto.getTitle());
   post.setContent(postDto.getContent());
   post.setDescription(postDto.getDescription());
   Post updatePost =  postRepository.save(post);
   PostDto dto =  mapToDto(updatePost);
   return dto;
    }

    @Override
    public PostDto getPostById(long id) {
       Post post =  postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Post not found with id"+id)
        );
    PostDto dto =  mapToDto(post);
    return dto;
    }

    @Override
//    public List<PostDto> getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//        Sort.Direction direction = Sort.Direction.ASC; // Default sort direction is ascending
//
//        if ("desc".equalsIgnoreCase(sortDir)) {
//            direction = Sort.Direction.DESC; // Set sort direction to descending if "sortDir" is "desc"
//        }
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize, direction, sortBy);
//        Page<Post> pagePosts = postRepository.findAll(pageable);
//
//        List<Post> posts = pagePosts.getContent();
//        List<PostDto> postDtos = posts.stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//
//        return postDtos;// Homework this code is from chatGpt
//    }
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? // This line will give ascending, if ascending equals ascending true
                Sort.by(sortBy).ascending(): // If it is true this will perform
                Sort.by(sortBy).descending(); // If it is false then it will perform ,, So this ternary operator will reduces number of lines of code
        Pageable pageable =  PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Post> pagePosts = postRepository.findAll(pageable);


        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        return postResponse;
    }

//    public List<PostDto> getPosts() {
//        List<Post> posts = postRepository.findAll();
//
//
//        List<PostDto> postDtos =  posts.stream().map(post->mapToDto(post).collect(Collectors.toList()));
//        return postDtos;
//    }

    PostDto  mapToDto(Post post){  //This method will convert this entity object to Dto ,, so that the code will become reusable
    PostDto dto = modelMapper.map(post, PostDto.class); //here all the content from the dto object will go to PostDto object in one line we reduce below commented lines.

        //            PostDto dto = new PostDto();//Bcz every time we need to repeat the step ,putt it in one method give the entity object and that converted into Dto
//            dto.setId(post.getId());
//            dto.setTitle(post.getTitle());
//            dto.setDescription(post.getDescription());
//            dto.setContent(post.getContent());
            return dto;
        }

       Post mapToEntity(PostDto postDto){

        Post post = modelMapper.map(postDto, Post.class);
           //.class will automatically create an object of postEntity copies the content.
//            Post post = new Post();
//            post.setTitle(postDto.getTitle());
//            post.setDescription(postDto.getDescription());
//            post.setContent(postDto.getContent());
            return post;
    }


        }




