import HTTP from "@/common/http";

const resource = "users";

function createParams(query, sort) {
  const params = new URLSearchParams();
  if (query) {
    for (let i = 0; i < query.length; i++) {
      params.append(query[i].name, query[i].value);
    }
  }
  if (sort) params.append("sort", sort);
  return params.toString();
}

async function find(url, query, sort) {
  const paramsStr = createParams(query, sort);
  if (paramsStr) url += "?" + paramsStr;
  const response = await HTTP.get(url);
  return response.data;
}

export default {
  async findAll() {
    const response = await HTTP.get(`${resource}`);
    return response.data;
  },
  async findActive() {
    //active users include unverified, verified and admin users
    const response = await HTTP.get(`${resource}/active`);
    return response.data;
  },
  async findUnverified() {
    const response = await HTTP.get(`${resource}/unverified`);
    return response.data;
  },
  async findSuspended() {
    const response = await HTTP.get(`${resource}/suspended`);
    return response.data;
  },
  async findVerified() {
    const response = await HTTP.get(`${resource}/verified`);
    return response.data;
  },
  async findAdmin() {
    const response = await HTTP.get(`${resource}/admin`);
    return response.data;
  },
  async findOne(id) {
    const response = await HTTP.get(`${resource}/${id}`);
    return response.data;
  },
  async findOneBase(id) {
    const response = await HTTP.get(`${resource}/${id}/base`);
    return response.data;
  },
  async saveUserImage(id, file) {
    const formData = new FormData();
    formData.append("file", file);
    return (
      await HTTP.post(`${resource}/${id}/image`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
    ).data;
  },
  async findUserEvents(id, query, sort) {
    return find(`${resource}/${id}/events`, query, sort);
  },
  async findUserUpcomingEvents(id, query, sort) {
    return find(`${resource}/${id}/events/upcoming`, query, sort);
  },
  async findUserPastEvents(id, query, sort) {
    return find(`${resource}/${id}/events/past`, query, sort);
  },
  async findUserUnreviewedEvents(id, query, sort) {
    return find(`${resource}/${id}/events/unreviewed`, query, sort);
  },
  async findUserRejectedEvents(id, query, sort) {
    return find(`${resource}/${id}/events/rejected`, query, sort);
  },
  async findUserSavedUpcomingEvents(id, query, sort) {
    return find(`${resource}/${id}/events/saved/upcoming`, query, sort);
  },
  async findUserSavedPastEvents(id, query, sort) {
    return find(`${resource}/${id}/events/saved/past`, query, sort);
  },
  async findUserSubscribedUpcomingEvents(id, query, sort) {
    return find(`${resource}/${id}/events/subscribed/upcoming`, query, sort);
  },
  async findUserSubscribedPastEvents(id, query, sort) {
    return find(`${resource}/${id}/events/subscribed/past`, query, sort);
  },
  async findUserSubscribedCategories(id, query, sort) {
    return find(`${resource}/${id}/categories/subscribed`, query, sort);
  },
  async findUserFollowers(id, query, sort) {
    return find(`${resource}/${id}/followers`, query, sort);
  },
  async findUserFollowing(id, query, sort) {
    return find(`${resource}/${id}/following`, query, sort);
  },
  async save(user) {
    if (user.id) {
      return (await HTTP.put(`${resource}/${user.id}`, user)).data;
    } else {
      return (await HTTP.post(`${resource}`, user)).data;
    }
  },
  async saveAuthority(user) {
    return (await HTTP.put(`${resource}/${user.id}/authority`, user)).data;
  },
};
