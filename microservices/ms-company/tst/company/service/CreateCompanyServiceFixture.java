package company.service;

import java.util.function.Supplier;

import company.model.Company;
import company.service.CreateCompanyService.CreateCompanyRequest;

public abstract class CreateCompanyServiceFixture {

    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_VALID = ()-> new CreateCompanyRequest("name-1");
    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_NULL = ()-> null;
    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_WITH_NULL_NAME = ()-> new CreateCompanyRequest(null);
    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_WITH_EMPTY_NAME = ()-> new CreateCompanyRequest("");
    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_WITH_BLANK_NAME = ()-> new CreateCompanyRequest("                     ");
    protected final  Supplier<CreateCompanyRequest> CREATE_COMPANY_REQUEST_WITH_NAME_MORE_THAN_50_CARACT = ()->  new CreateCompanyRequest("a".repeat(55));

    protected final Supplier<Company> CREATE_COMPANY_MODEL_VALID = ()->{
            Company company = new Company();
            company.setName("name-1");
            company.setId("id-1");
            return company;
    };
    

}
