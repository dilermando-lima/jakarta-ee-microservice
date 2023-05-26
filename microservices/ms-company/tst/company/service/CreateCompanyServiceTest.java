package company.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import core.rest.exception.BadRequesException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class CreateCompanyServiceTest extends CreateCompanyServiceFixture {

    @InjectMocks
    CreateCompanyService createCompanyService;

    @Mock
    EntityManager em;


    @Test
    void GIVEN_request_is_valid_WHEN_validateRequest_SHOULD_run_successfully(){
        var companyRequestValid = CREATE_COMPANY_REQUEST_VALID.get();
        assertDoesNotThrow(
            ()-> createCompanyService.validateRequest(companyRequestValid)
        );
    }


    @Test
    void GIVEN_request_is_null_WHEN_validateRequest_SHOULD_throw_bad_request_exception(){
        var companyRequestNull = CREATE_COMPANY_REQUEST_NULL.get();
        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateRequest(companyRequestNull)
        );
    }

    @Test
    void GIVEN_request_with_empty_name_WHEN_validateRequest_SHOULD_throw_bad_request_exception(){
        var companyRequestWithEmptyName = CREATE_COMPANY_REQUEST_WITH_EMPTY_NAME.get();
        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateRequest(companyRequestWithEmptyName)
        );
    }

    @Test
    void GIVEN_request_with_null_name_WHEN_validateRequest_SHOULD_throw_bad_request_exception(){
        var companyRequestWithNullName = CREATE_COMPANY_REQUEST_WITH_NULL_NAME.get();
        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateRequest(companyRequestWithNullName)
        );
    }

    @Test
    void GIVEN_request_with_blank_name_WHEN_validateRequest_SHOULD_throw_bad_request_exception(){
        var companyRequestWithBlankName = CREATE_COMPANY_REQUEST_WITH_BLANK_NAME.get();
        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateRequest(companyRequestWithBlankName)
        );
    }

    @Test
    void GIVEN_request_with_name_more_than_55_carac_WHEN_validateRequest_SHOULD_throw_bad_request_exception(){
        var companyRequestWithNameMoreThan50Caract = CREATE_COMPANY_REQUEST_WITH_NAME_MORE_THAN_50_CARACT.get();
        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateRequest(companyRequestWithNameMoreThan50Caract)
        );
    }

    @Test
    void GIVEN_valid_request_WHEN_convertRequestToModel_SHOULD_return_valid_CompanyModel(){

        var companyRequestValid = CREATE_COMPANY_REQUEST_VALID.get();
        var companyModelResult = createCompanyService.convertRequestToModel(companyRequestValid);

        assertNotNull(companyModelResult);
        assertEquals(companyModelResult.getName(),companyRequestValid.name());
        assertNull(companyModelResult.getId());
    }

    @Test
    void GIVEN_valid_companyModel_WHEN_convertModelToResponse_SHOULD_return_valid_response(){

        var companyModel = CREATE_COMPANY_MODEL_VALID.get();
        var responseResult = createCompanyService.convertModelToResponse(companyModel);

        assertNotNull(responseResult);
        assertEquals(responseResult.id(),companyModel.getId());
        assertEquals(responseResult.name(),companyModel.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void GIVEN_company_already_exists_WHEN_validateCompanyAlreadyExist_SHOULD_throw_bad_request_exception(){


        var companyRequestValid = CREATE_COMPANY_REQUEST_VALID.get();

        var queryMock = (TypedQuery<Long>) mock(TypedQuery.class);

        var ammountCompanyAlreadyContains = 1L;

        when((queryMock).getSingleResult()).thenReturn(ammountCompanyAlreadyContains);
        when((queryMock).setMaxResults(1)).thenReturn(queryMock);
        when((queryMock).setParameter("name", companyRequestValid.name())).thenReturn(queryMock);
        when(em.createQuery(anyString(),eq(Long.class))).thenReturn(queryMock);

        assertThrows(
            BadRequesException.class,
            ()->createCompanyService.validateCompanyAlreadyExist(companyRequestValid)
        );
    }


    @Test
    @SuppressWarnings("unchecked")
    void GIVEN_company_dosent_already_exists_WHEN_validateCompanyAlreadyExist_SHOULD_run_successfully(){

        var companyRequestValid = CREATE_COMPANY_REQUEST_VALID.get();

        var queryMock = (TypedQuery<Long>) mock(TypedQuery.class);

        var ammountCompanyAlreadyContains = 0L;

        when((queryMock).getSingleResult()).thenReturn(ammountCompanyAlreadyContains);
        when((queryMock).setMaxResults(1)).thenReturn(queryMock);
        when((queryMock).setParameter("name", companyRequestValid.name())).thenReturn(queryMock);
        when(em.createQuery(anyString(),eq(Long.class))).thenReturn(queryMock);

        assertDoesNotThrow(()->createCompanyService.validateCompanyAlreadyExist(companyRequestValid));
    }

    @Test
    void GIVEN_company_valid_WHEN_persistModel_SHOULD_run_successfully(){
        var companyModel = CREATE_COMPANY_MODEL_VALID.get();
        var transactionMock = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(transactionMock);
        assertDoesNotThrow(()-> createCompanyService.persistModel(companyModel));
    }

    @Test
    void GIVEN_valid_request_WHEN_create_SHOULD_run_successfully(){
        var companyRequestValid = CREATE_COMPANY_REQUEST_VALID.get();
        createCompanyService = mock(CreateCompanyService.class);
        when(createCompanyService.create(companyRequestValid)).thenCallRealMethod();
        assertDoesNotThrow(()-> createCompanyService.create(companyRequestValid));
    }

    
}
