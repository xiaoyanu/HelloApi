// 用于分页
export interface Pagination {
    // 当前页码
    page: number;
    // 每页显示数量
    pageSize: number;
    // 总条数
    total: number;
}