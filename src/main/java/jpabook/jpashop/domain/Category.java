package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
* 본인을 계층구조의 타입으 구현할 수 있음.
* */

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //JoinTable 필요함 (객체는 커넥션관계를 양쪽에서 갖질수 있지만
    //관계형 디비는 커넥션관계를 양쪽으로 가질 수 없어서 다대일, 일대다로 중간 테이블을 통해
    //양쪽으로 연결해줘야 양방향이 가능하다.
    //단, 딱 연결 상태로만 사용이 가능하고 필드 추가가 불가능하기에 실무에선 사용을 권장하지 않음.
    //JoinColums은 JoinTable에 존재하는 카테코리 테이블 연결 컬럼을 의미함.
    //InverseJoinColums은 Item 테이블과 연결할 JoinTable에 존재하는 컬럼을 의미함.
    @ManyToMany
    @JoinTable(name = "category_item",
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();


    //같은 엔티티에 대해서 양방향 연관관계를 걸었다고 보면 된다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    //mappedBy를 parent 객체명 그대로 작성해야 됨.
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //연관관계 메소드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
