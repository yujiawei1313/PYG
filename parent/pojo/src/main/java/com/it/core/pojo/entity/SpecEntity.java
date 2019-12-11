package com.it.core.pojo.entity;
import com.it.core.pojo.specification.Specification;
import com.it.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义封装的规格和规格选项集合实体对象
 */
public class SpecEntity implements Serializable {

    //规格对象
    private Specification specification;
    //规格选项集合
    private List<SpecificationOption> specificationOptionList;

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
