package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.shop.dto.ItemImgDto;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

/**
 * 상품 등록
 */

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();//상품 등록 폼으로부터 입력받은 데이터를 이용해 item객체 생성
        itemRepository.save(item); //상품 데이터 저장

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0) //첫번째 이미지일 경우
                itemImg.setRepimgYn("Y"); //대표 상품 이미지 여부를 Y로 세팅
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //상품 이미지 정보 저장

        }

        return item.getId();
    }

    //등록된 상품을 불러오는 메소드
    @Transactional(readOnly = true) //상품 데이터를 읽어오는 트랜잭션을 읽기전용으로 -> 더티체킹(변경감지)수행하지 않아서 성능 향상
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        //조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);//of(): null이 아닌 명시된 값을 가지는 Optional 객체를 반환
            itemImgDtoList.add(itemImgDto);
        }

        //상품의 id를 통해 상품 엔티티를 조회, 존재하지 않으면 EntityNotFoundException 발생
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

 /*   //상품 수정 페이지로 진입 나중에 등록,수정 페이지 나눌것
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); //조회한 상품 데이터
            model.addAttribute("itemFormDto", itemFormDto);

        } catch (EntityNotFoundException e) { //상품엔티티 없으면
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            //상품 등록 페이지로
            return "item/itemForm";

        }
        return "item/itemForm";

    }*/


    //상품 업데이트 (변경감지)
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto); //상품 등록 화면으로부터 전달받은 ItemFormDto를 통해 상품 엔티티 업데이트

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트 조회

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            //상품 이미지를 업데이트 하기 위해 updateItemImg()메소드에 상품 이미지 아이디와, 파일 정보를 파라미터로 전달
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }



}