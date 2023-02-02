package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터 추가
     * PostConstruct 는 construct 실행 후 동작(bean 의 등록 이후)
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("ItemA", 10000, 10));
        itemRepository.save(new Item("ItemB", 20000, 20));
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam int quantity,
                            Model model) {
        Item item = new Item(itemName, price, quantity);

        /*
         item.setItemName(itemName);
         item.setPrice(price);
         item.setQuantity(quantity);
        */

        Item saveItem = itemRepository.save(item);
        model.addAttribute("item", saveItem);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        // @ModelAttribute 자동으로 입력받은 key 값으로 model addAttribute 를 해준다. => model.addAttribute("item", item)
        // model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // @ModelAttribute 는 객체이름을 소문자로 치환해서 key로 한다.
        // Ex)Item -> item 소문자로 치환한 후 해당하는 치환된 소문자를 키 값으로 해서 model 에 Set 해줌
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        // @ModelAttribute 생략 가능, 얘만 가능함 RequestBody 같은 얘를 생략하면 RequestBody 가 되는게 아니고
        // @ModelAttribute 가 무조건 낚아챔
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        // html 리턴에서 redirect 로 변경 (새로고침시 리다이렉트가 된 get 이 마지막 요청이 되도록 변경)
        // 이전의 경우에는 새로고침 시 다시 post요청이 들어온다. (이유는 return html 을 던져준거이기 때문이다. 요청 url이 변경된적이 없다.)
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        // RedirectAttributes 기존에 string 에 + 하는 연산을 하는 방식에서 더 나아가 인코딩 및 보기편하게 redirect에 관련 속성을 정의하는 것을 도와준다.
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // queryparameter 로 들어간다.
        return "redirect:/basic/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
}
