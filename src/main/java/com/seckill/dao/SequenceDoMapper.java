package com.seckill.dao;

import com.seckill.dataObject.SequenceDo;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    int insert(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    int insertSelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    SequenceDo selectByPrimaryKey(String name);

    SequenceDo getSequenceByName(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    int updateByPrimaryKeySelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Tue Nov 30 09:58:46 CST 2021
     */
    int updateByPrimaryKey(SequenceDo record);
}