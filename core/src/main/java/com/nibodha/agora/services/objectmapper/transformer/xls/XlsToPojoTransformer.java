package com.nibodha.agora.services.objectmapper.transformer.xls;

import com.nibodha.agora.services.objectmapper.config.Field;
import com.nibodha.agora.services.objectmapper.config.Mapping;
import com.nibodha.agora.services.objectmapper.support.MapperObject;
import com.nibodha.agora.services.objectmapper.transformer.Transformer;
import com.nibodha.agora.services.objectmapper.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class XlsToPojoTransformer implements Transformer<File, List<Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlsToPojoTransformer.class);

    @Override
    public List<Object> transform(final File file, final Mapping mappingConfig) throws Exception {
        Validate.notNull(mappingConfig, "A mapping configuration must be provided!");
        Validate.notNull(file, "Stream source must be provided!");

        final List<Object> destination = new ArrayList<>();

        final Workbook workbook = WorkbookFactory.create(file);
        final Sheet sheet = workbook.getSheetAt(mappingConfig.getSheet());

        final List<String> columnHeaders = getColumnHeaders(mappingConfig, sheet);

        for (int rowIndex = mappingConfig.getDataStartRow(); rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
            final Row row = sheet.getRow(rowIndex);
            final Object rowObj = transformRowCells(row, mappingConfig, columnHeaders);
            destination.add(rowObj);
        }

        workbook.close();

        return destination;
    }

    protected Object transformRowCells(final Row row, final Mapping mappingConfig, final List<String> columnHeaders) {
        final Object result;
        final Map<String, Cell> cellValues = getCellValues(row, columnHeaders);

        if (StringUtils.isNotBlank(mappingConfig.getDestination()) && mappingConfig.getFields() != null) {
            LOGGER.info("Xls to Pojo Conversion ::: Missing field mapping - converting to Map by default for {}", mappingConfig.getSource());
            result = transformRowToBean(mappingConfig, columnHeaders, cellValues);
        } else {
            result = transformRowToMap(cellValues, columnHeaders);
        }
        return result;
    }

    private Object transformRowToBean(final Mapping mappingConfig, List<String> columnHeaders, final Map<String, Cell> cellValues) {
        final MapperObject rowObj = new MapperObject(ObjectUtils.newInstance(mappingConfig.getDestination()));
        final List<Field> fieldMappings = mappingConfig.getFields().getField();
        fieldMappings.forEach(field -> {
            final Cell cell = cellValues.get(field.getSource());
            if (cell != null) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                rowObj.setValue(field.getDestination(), cell.getStringCellValue());
            } else if (!columnHeaders.contains(field.getSource())) {
                LOGGER.warn("Xls to Pojo Conversion ::: Cannot find column header in excel for {}", field.getSource());
            }
        });
        return rowObj.getObject();
    }

    private Object transformRowToMap(final Map<String, Cell> cellValues, final List<String> columnHeaders) {
        final Map<String, String> resultMap = new HashMap<>();
        columnHeaders.forEach(column -> {
            final Cell cell = cellValues.get(column);
            if (cell != null) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                resultMap.put(column, cell.getStringCellValue());
            } else {
                resultMap.put(column, null);
            }
        });
        return resultMap;
    }

    private List<String> getColumnHeaders(final Mapping mappingConfig, final Sheet sheet) {
        final List<String> headers = new ArrayList<>();
        final Row headerRow = sheet.getRow(mappingConfig.getHeaderRow());
        headerRow.forEach(cell -> {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            headers.add(cell.getStringCellValue());
        });
        return headers;
    }

    private Map<String, Cell> getCellValues(final Row row, final List<String> columnHeaders) {
        final Map<String, Cell> cellValues = new HashMap<>(row.getPhysicalNumberOfCells());
        row.forEach(cell -> cellValues.put(columnHeaders.get(cell.getColumnIndex()), cell));
        return cellValues;
    }

}
