package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

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

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            //imgName:실제로컬에 저장된 상품 이미지 파일 이름, oriImgName: 업로드했던 상품 이미지파일 원래 이름
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName; //업로드 결과 로컬에 저장된 상품 이미지 파일 불러오는 경로
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    //상품 이미지 수정, 변경감지 기능 사용
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        //상품 이미지 수정한 경우 상품 이미지를 업데이트
        if (!itemImgFile.isEmpty()) {
            //상품이미지 아이디를 이용해 기존에 저장했던 상품 이미지 엔티티 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new); //없으면 예외처리

            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" +
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //수정한 이미지 파일 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl); //변경된 상품 이미지 정보 세팅
            //상품 등록 때 처럼 save()로직 호출하지 않는다. savedItemImg엔티티는 현재 영속 상태 이므로 데이터를 변경하는 것만으로도 변경 감지 기능 동작


        }


    }


}
