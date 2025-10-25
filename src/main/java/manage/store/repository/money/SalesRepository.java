package manage.store.repository.money;

import manage.store.exception.common.db.DatabaseOperationException;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.money.sales.DailySales.StoreSales;

import java.util.List;

public interface SalesRepository {

    /**
     * 특정 월의 매출 정보 조회
     * @param branchCd 지점 코드 <br>
     * @param year 조회할 연도
     * @return List<Sales> - 매출 정보 리스트
     * @throws InvalidParameterException 잘못된 파라미터
     */
    List<StoreSales> selectSalesByYear(String branchCd, Integer year);

    /**
     * 특정 월의 매출 정보 조회
     * @param branchCd 지점 코드 <br>
     * @param year 조회할 연도
     * @param month 조회할 월
     * @return List<Sales> - 매출 정보 리스트
     * @throws InvalidParameterException 잘못된 파라미터
     */
    List<StoreSales> selectSalesByMonth(String branchCd, Integer year, Integer month);


    /**
     * 매출 정보 등록
     * @param sales 매출 정보
     * @return int - 등록된 매출 수
     * @throws InvalidParameterException 잘못된 파라미터
     * @throws DatabaseOperationException 데이터베이스 작업 실패
     */
    int insertSales(StoreSales sales);

    /**
     * 매출 정보 업데이트
     * @param sales 매출 정보
     * @return int - 업데이트된 매출 수
     * @throws InvalidParameterException 잘못된 파라미터
     * @throws DatabaseOperationException 데이터베이스 작업 실패
     */
    int updateSales(StoreSales sales);

}
