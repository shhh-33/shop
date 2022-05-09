package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

/**
 * 상품 이미지 업로드, 상품 이미지 정보 저장
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}") //properties파일에 등록한 itemImgLocation 값을 불러온다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //imgName:실제로컬에 저장된 상품 이미지 파일 이름, oriImgName: 업로드했던 상품 이미지파일 원래 이름
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                                              itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName; //업로드 결과 로컬에 저장된 상품 이미지 파일 불러오는 경로
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }





}
