declare namespace API {
  type AIRequest = {
    userMessage?: string
  }

  type AIResponse = {
    output?: Output4
    requestId?: string
    usage?: Usage
  }

  type BaseResponseAIResponse_ = {
    code?: number
    data?: AIResponse
    msg?: string
  }

  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    msg?: string
  }

  type BaseResponseCreateOutPaintingTaskResponse_ = {
    code?: number
    data?: CreateOutPaintingTaskResponse
    msg?: string
  }

  type BaseResponseCreatePictureTaskResponse_ = {
    code?: number
    data?: CreatePictureTaskResponse
    msg?: string
  }

  type BaseResponseGetOutPaintingTaskResponse_ = {
    code?: number
    data?: GetOutPaintingTaskResponse
    msg?: string
  }

  type BaseResponseGetPictureTaskResponse_ = {
    code?: number
    data?: GetPictureTaskResponse
    msg?: string
  }

  type BaseResponseInt_ = {
    code?: number
    data?: number
    msg?: string
  }

  type BaseResponseListGalleryStatisticsVo_ = {
    code?: number
    data?: GalleryStatisticsVo[]
    msg?: string
  }

  type BaseResponseListImageSearchResult_ = {
    code?: number
    data?: ImageSearchResult[]
    msg?: string
  }

  type BaseResponseListPictureVO_ = {
    code?: number
    data?: PictureVO[]
    msg?: string
  }

  type BaseResponseListSpace_ = {
    code?: number
    data?: Space[]
    msg?: string
  }

  type BaseResponseListSpaceCategoryAnalyzeResponse_ = {
    code?: number
    data?: SpaceCategoryAnalyzeResponse[]
    msg?: string
  }

  type BaseResponseListSpaceLevel_ = {
    code?: number
    data?: SpaceLevel[]
    msg?: string
  }

  type BaseResponseListSpaceSizeAnalyzeResponse_ = {
    code?: number
    data?: SpaceSizeAnalyzeResponse[]
    msg?: string
  }

  type BaseResponseListSpaceTagAnalyzeResponse_ = {
    code?: number
    data?: SpaceTagAnalyzeResponse[]
    msg?: string
  }

  type BaseResponseListSpaceUserAnalyzeResponse_ = {
    code?: number
    data?: SpaceUserAnalyzeResponse[]
    msg?: string
  }

  type BaseResponseListSpaceUserVO_ = {
    code?: number
    data?: SpaceUserVO[]
    msg?: string
  }

  type BaseResponseLoginUserVO_ = {
    code?: number
    data?: LoginUserVO
    msg?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    msg?: string
  }

  type BaseResponsePagePicture_ = {
    code?: number
    data?: PagePicture_
    msg?: string
  }

  type BaseResponsePagePictureVO_ = {
    code?: number
    data?: PagePictureVO_
    msg?: string
  }

  type BaseResponsePagePostVo_ = {
    code?: number
    data?: PagePostVo_
    msg?: string
  }

  type BaseResponsePageReplyVo_ = {
    code?: number
    data?: PageReplyVo_
    msg?: string
  }

  type BaseResponsePageSpace_ = {
    code?: number
    data?: PageSpace_
    msg?: string
  }

  type BaseResponsePageSpaceVO_ = {
    code?: number
    data?: PageSpaceVO_
    msg?: string
  }

  type BaseResponsePageUserVO_ = {
    code?: number
    data?: PageUserVO_
    msg?: string
  }

  type BaseResponsePictureTagCategory_ = {
    code?: number
    data?: PictureTagCategory
    msg?: string
  }

  type BaseResponsePictureVO_ = {
    code?: number
    data?: PictureVO
    msg?: string
  }

  type BaseResponsePostVo_ = {
    code?: number
    data?: PostVo
    msg?: string
  }

  type BaseResponseSpace_ = {
    code?: number
    data?: Space
    msg?: string
  }

  type BaseResponseSpaceUsageAnalyzeResponse_ = {
    code?: number
    data?: SpaceUsageAnalyzeResponse
    msg?: string
  }

  type BaseResponseSpaceUser_ = {
    code?: number
    data?: SpaceUser
    msg?: string
  }

  type BaseResponseSpaceVO_ = {
    code?: number
    data?: SpaceVO
    msg?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    msg?: string
  }

  type BaseResponseUser_ = {
    code?: number
    data?: User
    msg?: string
  }

  type BaseResponseUserVO_ = {
    code?: number
    data?: UserVO
    msg?: string
  }

  type CategoryCountVo = {
    category?: string
    count?: number
  }

  type Choice = {
    finish_reason?: string
    message?: Message
  }

  type CreateAIPictureTaskRequest = {
    parameters?: Parameters1
    prompt?: string
  }

  type CreateOutPaintingTaskResponse = {
    code?: string
    message?: string
    output?: Output
    requestId?: string
  }

  type CreatePictureOutPaintingTaskRequest = {
    parameters?: Parameters
    pictureId?: number
  }

  type CreatePictureTaskResponse = {
    code?: string
    message?: string
    output?: Output2
    requestId?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type GalleryStatisticsVo = {
    categoryCounts?: CategoryCountVo[]
    galleryName?: string
  }

  type GetOutPaintingTaskResponse = {
    output?: Output1
    requestId?: string
  }

  type getPictureOutPaintingTaskUsingGETParams = {
    /** taskId */
    taskId?: string
  }

  type GetPictureTaskResponse = {
    output?: Output3
    requestId?: string
  }

  type getPictureTaskUsingGETParams = {
    /** taskId */
    taskId?: string
  }

  type getPictureVOByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getPostVOByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getSpaceByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getSpaceVOByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type ImageSearchResult = {
    fromUrl?: string
    thumbUrl?: string
  }

  type LoginUserVO = {
    createTime?: string
    editTime?: string
    id?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type MapStringString_ = true

  type Message = {
    content?: string
    role?: string
  }

  type Output = {
    taskId?: string
    taskStatus?: string
  }

  type Output1 = {
    code?: string
    endTime?: string
    message?: string
    outputImageUrl?: string
    scheduledTime?: string
    submitTime?: string
    taskId?: string
    taskMetrics?: TaskMetrics
    taskStatus?: string
  }

  type Output2 = {
    taskId?: string
    taskStatus?: string
  }

  type Output3 = {
    code?: string
    message?: string
    results?: MapStringString_[]
    taskId?: string
    taskMetrics?: TaskMetrics1
    taskStatus?: string
  }

  type Output4 = {
    choices?: Choice[]
  }

  type PagePicture_ = {
    current?: number
    pages?: number
    records?: Picture[]
    size?: number
    total?: number
  }

  type PagePictureVO_ = {
    current?: number
    pages?: number
    records?: PictureVO[]
    size?: number
    total?: number
  }

  type PagePostVo_ = {
    current?: number
    pages?: number
    records?: PostVo[]
    size?: number
    total?: number
  }

  type PageReplyVo_ = {
    current?: number
    pages?: number
    records?: ReplyVo[]
    size?: number
    total?: number
  }

  type PageSpace_ = {
    current?: number
    pages?: number
    records?: Space[]
    size?: number
    total?: number
  }

  type PageSpaceVO_ = {
    current?: number
    pages?: number
    records?: SpaceVO[]
    size?: number
    total?: number
  }

  type PageUserVO_ = {
    current?: number
    pages?: number
    records?: UserVO[]
    size?: number
    total?: number
  }

  type Parameters = {
    addWatermark?: boolean
    angle?: number
    bestQuality?: boolean
    bottomOffset?: number
    leftOffset?: number
    limitImageSize?: boolean
    outputRatio?: string
    rightOffset?: number
    topOffset?: number
    xScale?: number
    yScale?: number
  }

  type Parameters1 = {
    n?: number
    refMode?: string
    ref_strength?: number
    seed?: number
    size?: string
    style?: string
  }

  type Picture = {
    category?: string
    createTime?: string
    editTime?: string
    id?: number
    introduction?: string
    isDelete?: number
    name?: string
    picColor?: string
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    reviewMessage?: string
    reviewStatus?: number
    reviewTime?: string
    reviewerId?: number
    spaceId?: number
    tags?: string
    thumbnailUrl?: string
    updateTime?: string
    url?: string
    userId?: number
  }

  type PictureEditByBatchRequest = {
    category?: string
    nameRule?: string
    pictureIdList?: number[]
    spaceId?: number
    tags?: string[]
  }

  type PictureEditRequest = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
  }

  type PictureQueryRequest = {
    category?: string
    current?: number
    endEditTime?: string
    id?: number
    introduction?: string
    name?: string
    nullSpaceId?: boolean
    pageSize?: number
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    reviewMessage?: string
    reviewStatus?: number
    reviewerId?: number
    searchText?: string
    sortField?: string
    sortOrder?: string
    spaceId?: number
    startEditTime?: string
    tags?: string[]
    userId?: number
  }

  type PictureReviewRequest = {
    id?: number
    reviewMessage?: string
    reviewStatus?: number
  }

  type PictureTagCategory = {
    categoryList?: string[]
    tagList?: string[]
  }

  type PictureUpdateRequest = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
  }

  type PictureUploadByBatchRequest = {
    count?: number
    namePrefix?: string
    searchText?: string
  }

  type PictureUploadRequest = {
    fileUrl?: string
    id?: number
    picName?: string
    spaceId?: number
  }

  type PictureVO = {
    category?: string
    createTime?: string
    editTime?: string
    id?: number
    introduction?: string
    name?: string
    permissionList?: string[]
    picColor?: string
    picFormat?: string
    picHeight?: number
    picScale?: number
    picSize?: number
    picWidth?: number
    spaceId?: number
    tags?: string[]
    thumbnailUrl?: string
    updateTime?: string
    url?: string
    user?: UserVO
    userId?: number
  }

  type PostAddRequest = {
    category?: string[]
    content?: string[]
    title?: string
  }

  type PostQueryRequest = {
    category?: string[]
    content?: string[]
    createTime?: string
    current?: number
    heartCount?: number
    id?: number
    pageSize?: number
    replyCount?: number
    sortField?: string
    sortOrder?: string
    title?: string
    userId?: number
    viewCount?: number
  }

  type PostVo = {
    action?: string
    category?: string[]
    content?: string[]
    createTime?: string
    editTime?: string
    heartCount?: number
    id?: number
    isDelete?: number
    replyCount?: number
    title?: string
    updateTime?: string
    user?: UserVO
    viewCount?: number
  }

  type ReplyAddRequest = {
    content?: string
    id?: number
    postId?: number
  }

  type ReplyCommentAddRequest = {
    content?: string
    id?: number
    replyId?: number
  }

  type ReplyCommentVo = {
    action?: 'disliked' | 'liked'
    content?: string
    createTime?: string
    dislikeCount?: number
    editTime?: string
    id?: number
    isDelete?: number
    likeCount?: number
    receiveUser?: User
    replyId?: number
    updateTime?: string
    user?: User
  }

  type ReplyQueryRequest = {
    content?: string
    createTime?: string
    current?: number
    dislikeCount?: number
    editTime?: string
    id?: number
    isDelete?: number
    likeCount?: number
    pageSize?: number
    postId?: number
    sortField?: string
    sortOrder?: string
    updateTime?: string
    userId?: number
  }

  type ReplyTOCommentAddRequest = {
    content?: string
    receiveUserId?: number
    replyId?: number
  }

  type ReplyVo = {
    action?: 'disliked' | 'liked'
    content?: string
    createTime?: string
    dislikeCount?: number
    editTime?: string
    id?: number
    likeCount?: number
    postId?: number
    replyCommentVoList?: ReplyCommentVo[]
    updateTime?: string
    user?: UserVO
  }

  type SearchPictureByColorRequest = {
    picColor?: string
    spaceId?: number
  }

  type SearchPictureByPictureRequest = {
    pictureId?: number
  }

  type SendMailCodeRequest = {
    email?: string
  }

  type Space = {
    createTime?: string
    editTime?: string
    id?: number
    isDelete?: number
    maxCount?: number
    maxSize?: number
    spaceLevel?: number
    spaceName?: string
    spaceType?: number
    totalCount?: number
    totalSize?: number
    updateTime?: string
    userId?: number
  }

  type SpaceAddRequest = {
    spaceLevel?: number
    spaceName?: string
    spaceType?: number
  }

  type SpaceCategoryAnalyzeRequest = {
    queryAll?: boolean
    queryPublic?: boolean
    spaceId?: number
  }

  type SpaceCategoryAnalyzeResponse = {
    category?: string
    count?: number
    totalSize?: number
  }

  type SpaceEditRequest = {
    id?: number
    spaceLevel?: number
    spaceName?: string
  }

  type SpaceLevel = {
    maxCount?: number
    maxSize?: number
    text?: string
    value?: number
  }

  type SpaceQueryRequest = {
    current?: number
    id?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    spaceLevel?: number
    spaceName?: string
    spaceType?: number
    userId?: number
  }

  type SpaceRankAnalyzeRequest = {
    topN?: number
  }

  type SpaceSizeAnalyzeRequest = {
    queryAll?: boolean
    queryPublic?: boolean
    spaceId?: number
  }

  type SpaceSizeAnalyzeResponse = {
    count?: number
    sizeRange?: string
  }

  type SpaceTagAnalyzeRequest = {
    queryAll?: boolean
    queryPublic?: boolean
    spaceId?: number
  }

  type SpaceTagAnalyzeResponse = {
    count?: number
    tag?: string
  }

  type SpaceUpdateRequest = {
    id?: number
    maxCount?: number
    maxSize?: number
    spaceLevel?: number
    spaceName?: string
  }

  type SpaceUsageAnalyzeRequest = {
    queryAll?: boolean
    queryPublic?: boolean
    spaceId?: number
  }

  type SpaceUsageAnalyzeResponse = {
    countUsageRatio?: number
    maxCount?: number
    maxSize?: number
    sizeUsageRatio?: number
    usedCount?: number
    usedSize?: number
  }

  type SpaceUser = {
    createTime?: string
    id?: number
    spaceId?: number
    spaceRole?: string
    updateTime?: string
    userId?: number
  }

  type SpaceUserAddRequest = {
    spaceId?: number
    spaceRole?: string
    userId?: number
  }

  type SpaceUserAnalyzeRequest = {
    queryAll?: boolean
    queryPublic?: boolean
    spaceId?: number
    timeDimension?: string
    userId?: number
  }

  type SpaceUserAnalyzeResponse = {
    count?: number
    period?: string
  }

  type SpaceUserEditRequest = {
    id?: number
    spaceRole?: string
  }

  type SpaceUserQueryRequest = {
    id?: number
    spaceId?: number
    spaceRole?: string
    userId?: number
  }

  type SpaceUserVO = {
    createTime?: string
    id?: number
    space?: SpaceVO
    spaceId?: number
    spaceRole?: string
    updateTime?: string
    user?: UserVO
    userId?: number
  }

  type SpaceVO = {
    createTime?: string
    editTime?: string
    id?: number
    maxCount?: number
    maxSize?: number
    permissionList?: string[]
    spaceLevel?: number
    spaceName?: string
    spaceType?: number
    totalCount?: number
    totalSize?: number
    updateTime?: string
    user?: UserVO
    userId?: number
  }

  type TaskMetrics = {
    failed?: number
    succeeded?: number
    total?: number
  }

  type TaskMetrics1 = {
    failed?: number
    succeeded?: number
    total?: number
  }

  type testDownloadFileUsingGETParams = {
    /** filepath */
    filepath?: string
  }

  type uploadPictureUsingPOSTParams = {
    fileUrl?: string
    id?: number
    picName?: string
    spaceId?: number
  }

  type Usage = {
    input_tokens?: number
    output_tokens?: number
    total_tokens?: number
  }

  type User = {
    createTime?: string
    editTime?: string
    id?: number
    isDelete?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: string
  }

  type UserAddRequest = {
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    captcha?: string
    userAccount?: string
    userPassword?: string
  }

  type UserQueryRequest = {
    current?: number
    id?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    userAccount?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    checkPassword?: string
    code?: string
    userAccount?: string
    userPassword?: string
  }

  type UserUpdateRequest = {
    id?: number
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    createTime?: string
    id?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }
}
