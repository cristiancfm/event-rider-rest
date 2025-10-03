<template>
  <div class="container-fluid text-start p-4">
    <div class="row mb-2">
      <div class="col">
        <h2>
          <i class="bi bi-wrench-adjustable"></i>
          Administration
        </h2>
      </div>
    </div>
    <div class="row">
      <div class="container-fluid">
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/admin/events"
          active-class="active"
        >
          Events
        </router-link>
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/admin/categories"
          active-class="active"
        >
          Categories
        </router-link>
        <router-link
          class="btn btn-secondary me-2 mb-2"
          to="/admin/members"
          active-class="active"
        >
          Members
        </router-link>
      </div>
    </div>
    <br />
    <router-view />
  </div>
</template>

<script>
export default {
  name: "AdminDetail",
};
</script>

<style scoped></style>
