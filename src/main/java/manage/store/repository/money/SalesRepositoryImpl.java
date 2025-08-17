package manage.store.repository.money;


import lombok.RequiredArgsConstructor;
import manage.store.repository.BaseRepository;
import manage.store.utils.DateUtils;
import manage.store.exception.common.db.DatabaseOperationException;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.money.sales.DailySales.DailySales;
import manage.store.repository.money.mapper.SalesMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SalesRepositoryImpl extends BaseRepository implements SalesRepository {

    private final SalesMapper salesMapper;

    @Override
    public List<DailySales> selectSalesByYear(String branchCd, Integer year) {
        if(!isSelectSalesParamValid(branchCd, year)){
            throw new InvalidParameterException("Invalid parameters for selecting sales. Branch code: " + branchCd + ", Registered date: " + year);
        }

        try {
            return salesMapper.selectByYear(branchCd, year);
        } catch (Exception e) {
            handleAndWrapException(e, "selectSalesByYear", new Object[]{branchCd, year});
            return null;
        }
    }

    @Override
    public List<DailySales> selectSalesByMonth(String branchCd, Integer year, Integer month) {
        if(!isSelectSalesParamValid(branchCd, year, month)){
            throw new InvalidParameterException("Invalid parameters for selecting sales. Branch code: " + branchCd + ", Registered date: " + year + ", Month: " + month);
        }

        try {
            return salesMapper.selectByMonth(branchCd, year, month);
        } catch (Exception e) {
            handleAndWrapException(e, "selectSalesByMonth", new Object[]{branchCd, year, month});
            return null;
        }
    }

    @Override
    public int insertSales(DailySales sales) {
        if(!isUpdateSalesParamValid(sales)){
            throw new InvalidParameterException("Invalid sales data provided for insertion. Parameters: " + sales);
        }

        int insertedCnt = 0;
        try{
            insertedCnt = salesMapper.insert(sales);
            if(insertedCnt == 0 || insertedCnt > 1) {
                throw new DataIntegrityViolationException("Failed to insert sales data. Updated count: " + insertedCnt);
            }
            return insertedCnt;
        } catch (Exception e) {
            handleAndWrapException(e, "insertSales", new Object[]{sales});
            return 0; // In case of exception, return 0 to indicate failure
        }
    }

    @Override
    public int updateSales(DailySales sales) {
        if(!isUpdateSalesParamValid(sales)){
            throw new InvalidParameterException("Invalid sales data provided for update. Parameters: " + sales);
        }

        int updatedCnt = 0;
        try{
            updatedCnt = salesMapper.update(sales);
            if(updatedCnt == 0 || updatedCnt > 1) {
                throw new DataIntegrityViolationException("Failed to update sales data. Updated count: " + updatedCnt);
            }
            return updatedCnt;
        } catch (Exception e) {
            handleAndWrapException(e, "updateSales", new Object[]{sales});
            return 0; // In case of exception, return 0 to indicate failure
        }
    }

    private boolean isSelectSalesParamValid(String branchCd, Integer year, Integer month) {
        return isSelectSalesParamValid(branchCd, year) && DateUtils.isMonthValid(month);
    }

    private boolean isSelectSalesParamValid(String branchCd, Integer year) {
        return StringUtils.hasText(branchCd) && DateUtils.isYearValid(year);
    }

    private boolean isUpdateSalesParamValid(DailySales sales) {
        return sales != null && sales.isValid();
    }
}
