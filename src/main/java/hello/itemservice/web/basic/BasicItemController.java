package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {

    private final ItemRepository itemRepository;
    //@Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    /**
     * 테스트용 데이터 추가
     *  - 의존성 주입이 일어난 후 발생(@PostConstruct)
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }



    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        log.info("call /basic/items/{itemId} :: itemId = {}" , itemId);
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    @PostMapping("/addV1")
    public String addItemV1(@RequestParam String itemName, @RequestParam Integer price, @RequestParam Integer quantity,
                       Model model){
        Item item = new Item(itemName,price,quantity);
        //item.setItemName(itemName);
        //item.setPrice(price);
        //item.setQuantity(quantity);
        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/item";
    }


    @PostMapping("/addV2")
    public String addItemV2(@ModelAttribute("item") Item item/*, Model model*/){

        itemRepository.save(item);

        //@ModelAttribute에 name을 지정하면 자동으로 model에 추가 된다.
        //model.addAttribute("item",item);

        return "basic/item";
    }


    @PostMapping("/addV3")
    public String addItemV3(@ModelAttribute Item item){

        itemRepository.save(item);

        //@ModelAttribute에 name을 빼면 기본룰을 클래스명의 첫글자를 소문자로 바꿔서 넣어준다.
        //model.addAttribute("item",item);

        return "basic/item";
    }


    @PostMapping("/addV4")
    public String addItemV4(Item item){

        itemRepository.save(item);

        //@ModelAttribute 자체도 생략 가능하다.

        return "basic/item";
    }

    @PostMapping("/addV5")
    public String addItemV5(Item item){

        itemRepository.save(item);

        //return "basic/item";
        //V4방식으로 사용하면 post로 전송된 데이터가 계속 남아있어서
        //페이지 새로고침을 계속하게 되면 새로 상품이 등록된다.
        //redirect로 get방식으로 다시 호출하도록 하면 새로고침을 해도 get호출이 불리기 때문에
        //상품이 새로 저장되지 않는다.
        //PRG 방식(Post Redirect Get 방식)
        //RedirectAttributes를 사용하면 인코딩 문제도 해결할 수 있다.
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){

        Item savedItem = itemRepository.save(item);

        //RedirectAttributes를 사용하면
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        //return "redirect:/basic/items/" + savedItem.getId();
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Item item){

        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";

    }









}
