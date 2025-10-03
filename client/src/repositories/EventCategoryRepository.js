import HTTP from "@/common/http";

const resource = "event-categories";

export default {
  async findAll() {
    const response = await HTTP.get(resource);
    return response.data;
  },
  async findPublished() {
    const response = await HTTP.get(`${resource}/published`);
    return response.data;
  },
  async findUnreviewed() {
    const response = await HTTP.get(`${resource}/unreviewed`);
    return response.data;
  },
  async findRejected() {
    const response = await HTTP.get(`${resource}/rejected`);
    return response.data;
  },
  async findOne(id) {
    const response = await HTTP.get(`${resource}/${id}`);
    return response.data;
  },
  async save(eventCategory) {
    if (eventCategory.id) {
      return (await HTTP.put(`${resource}/${eventCategory.id}`, eventCategory))
        .data;
    } else {
      return (await HTTP.post(`${resource}`, eventCategory)).data;
    }
  },
  async delete(id) {
    return (await HTTP.delete(`${resource}/${id}`)).data;
  },
};
