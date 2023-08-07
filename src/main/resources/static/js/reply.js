async function get1(bno) {

    const result = await axios.get(`/replies/list/${bno}`)

    //console.log(result)

    return result;
}

async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/free/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }

    return result.data
}


async function addReply(replyObj) {
    const response = await axios.post(`/replies/free/register`,replyObj)
    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/replies/free/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/replies/free/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/free/${rno}`)
    return response.data
}
// async function get1(bno) {
//
//     const result = await axios.get(`/replies/report/list/${bno}`)
//
//     //console.log(result)
//
//     return result;
// }
//
// async function getList({bno, page, size, goLast}){
//
//     const result = await axios.get(`/replies/report/list/${bno}`, {params: {page, size}})
//
//     if(goLast){
//         const total = result.data.total
//         const lastPage = parseInt(Math.ceil(total/size))
//
//         return getList({bno:bno, page:lastPage, size:size})
//
//     }
//
//     return result.data
// }
//
//
// async function addReply(replyObj) {
//     const response = await axios.post(`/replies/report/register`,replyObj)
//     return response.data
// }
//
// async function getReply(rno) {
//     const response = await axios.get(`/replies/report/${rno}`)
//     return response.data
// }
//
// async function modifyReply(replyObj) {
//
//     const response = await axios.put(`/replies/report/${replyObj.rno}`, replyObj)
//     return response.data
// }
//
// async function removeReply(rno) {
//     const response = await axios.delete(`/replies/report/${rno}`)
//     return response.data
// }

