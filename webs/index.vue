<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="projectName">
            <el-input v-model="form.projectName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectLocation">
            <el-input v-model="form.projectLocation" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectBudget">
            <el-input v-model="form.projectBudget" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectArea">
            <el-input v-model="form.projectArea" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectType">
            <el-input v-model="form.projectType" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectCompleted">
            <el-input v-model="form.projectCompleted" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectShortdes">
            <el-input v-model="form.projectShortdes" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectDes">
            <el-input v-model="form.projectDes" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectBanner">
            <el-input v-model="form.projectBanner" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectBrochure">
            <el-input v-model="form.projectBrochure" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectOnhome">
            <el-input v-model="form.projectOnhome" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectCate">
            <el-input v-model="form.projectCate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="projectSort">
            <el-input v-model="form.projectSort" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="columns.visible('projectName')" prop="projectName" label="projectName" />
        <el-table-column v-if="columns.visible('projectLocation')" prop="projectLocation" label="projectLocation" />
        <el-table-column v-if="columns.visible('projectBudget')" prop="projectBudget" label="projectBudget" />
        <el-table-column v-if="columns.visible('projectArea')" prop="projectArea" label="projectArea" />
        <el-table-column v-if="columns.visible('projectType')" prop="projectType" label="projectType" />
        <el-table-column v-if="columns.visible('projectCompleted')" prop="projectCompleted" label="projectCompleted" />
        <el-table-column v-if="columns.visible('projectShortdes')" prop="projectShortdes" label="projectShortdes" />
        <el-table-column v-if="columns.visible('projectDes')" prop="projectDes" label="projectDes" />
        <el-table-column v-if="columns.visible('projectBanner')" prop="projectBanner" label="projectBanner" />
        <el-table-column v-if="columns.visible('projectBrochure')" prop="projectBrochure" label="projectBrochure" />
        <el-table-column v-if="columns.visible('projectOnhome')" prop="projectOnhome" label="projectOnhome" />
        <el-table-column v-if="columns.visible('projectCate')" prop="projectCate" label="projectCate" />
        <el-table-column v-if="columns.visible('projectSort')" prop="projectSort" label="projectSort" />
        <el-table-column v-if="columns.visible('id')" prop="id" label="id" />
        <el-table-column v-permission="['admin','constructionProject:edit','constructionProject:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudConstructionProject from '@/api/constructionProject'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";

// crud交由presenter持有
const defaultCrud = CRUD({ title: 'project', url: 'api/constructionProject', sort: 'id,desc', crudMethod: { ...crudConstructionProject }})
const defaultForm = { projectName: null, projectLocation: null, projectBudget: null, projectArea: null, projectType: null, projectCompleted: null, projectShortdes: null, projectDes: null, projectBanner: null, projectBrochure: null, projectOnhome: null, projectCate: null, projectSort: null, id: null }
export default {
  name: 'ConstructionProject',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      
      permission: {
        add: ['admin', 'constructionProject:add'],
        edit: ['admin', 'constructionProject:edit'],
        del: ['admin', 'constructionProject:del']
      },
      rules: {
      }    }
  },
  watch: {
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
  }
}



</script>

<style scoped>
  .table-img {
    display: inline-block;
    text-align: center;
    background: #ccc;
    color: #fff;
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    vertical-align: middle;
    width: 32px;
    height: 32px;
    line-height: 32px;
  }
</style>
